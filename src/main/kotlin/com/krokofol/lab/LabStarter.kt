package com.krokofol.lab

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.apache.commons.compress.compressors.CompressorInputStream
import org.apache.commons.compress.compressors.CompressorStreamFactory
import java.io.*
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.Attribute
import kotlin.RuntimeException

object LabStarter {
    private const val DEFAULT_INPUT_FILE_NAME = "input.bz2"
    private const val DEFAULT_OUTPUT_FILE_NAME = "output.xml"
    private const val DEFAULT_NUMBER_OF_SYMBOLS_TO_READ = "1000000"
    private const val DEFAULT_BUFFER_LENGTH = 100
    private const val CHAR_SIZE = 32

    fun start(args: Array<String>) {
        val commandLine = createCommandLine(args)
        val inputStream = getBufferedInputStream(commandLine)
        val xmlReader = getXmlReader(inputStream)
        val writer = getWriter(commandLine)
        val numberOfSymbolsToRead = getNumberOfSymbolsToRead(commandLine)

        val eventsPerUser = mutableMapOf<String, Int>()
        var nodesAmount = 0

        while (xmlReader.hasNext() && numberOfSymbolsToRead > inputStream.bytesRead) {
            val event = xmlReader.nextEvent()
            if (event.isStartElement && event.asStartElement().name.localPart == "node") {
                val userName = getUserName(event.asStartElement().attributes as Iterator<Attribute>)
                eventsPerUser.compute(userName) { _, value ->
                    value?.let { it + 1 } ?: 1
                }
                nodesAmount++
            }
        }

        writer.write("nodes amount - $nodesAmount\n")
        for (userData in eventsPerUser) {
            writer.write("${userData.key} has ${userData.value} ${if(userData.value > 1) "nodes" else "node"}\n")
        }
        writer.flush()
    }

    private fun getBufferedInputStream(commandLine: CommandLine): CompressorInputStream {
        val fileInputStream = FileInputStream(commandLine.getOptionValue("i", DEFAULT_INPUT_FILE_NAME))
        val bufferedInputStream = BufferedInputStream(fileInputStream, CHAR_SIZE * DEFAULT_BUFFER_LENGTH)
        return CompressorStreamFactory().createCompressorInputStream(bufferedInputStream)
    }

    private fun getXmlReader(input: CompressorInputStream): XMLEventReader {
        return XMLInputFactory.newInstance().createXMLEventReader(input)
            ?: throw RuntimeException("failed to create reader of xml file")
    }

    private fun getWriter(commandLine: CommandLine): BufferedWriter {
        val file = File(commandLine.getOptionValue("o", DEFAULT_OUTPUT_FILE_NAME))
        val fileWriter = FileWriter(file)
        return BufferedWriter(fileWriter, 32 * 100)
    }

    private fun getNumberOfSymbolsToRead(commandLine: CommandLine): Int {
        return commandLine.getOptionValue("n", DEFAULT_NUMBER_OF_SYMBOLS_TO_READ).toIntOrNull()
            ?: throw RuntimeException("value for option \"number\" is not number")
    }

    private fun createCommandLine(args: Array<String>): CommandLine {
        val options = Options()
        options.addOption("i", "input", true, "input file path")
        options.addOption("o", "output", true, "output file path")
        options.addOption("n", "number", true, "number of symbols to read")
        val commandLineParser = DefaultParser()
        return commandLineParser.parse(options, args)
    }

    private fun getUserName(attributes: Iterator<Attribute>): String {
        for (attribute in attributes) {
            if (attribute.name.localPart == "user") {
                return attribute.value
            }
        }
        throw RuntimeException("attribute name was not found in node")
    }
}

fun main(args: Array<String>) {
    LabStarter.start(args)
}