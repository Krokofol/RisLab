package com.krokofol.lab

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.apache.commons.compress.compressors.CompressorStreamFactory
import java.io.*
import java.lang.RuntimeException

object LabStarter {
    private const val DEFAULT_INPUT_FILE_NAME = "input.bz2"
    private const val DEFAULT_OUTPUT_FILE_NAME = "output.xml"
    private const val DEFAULT_NUMBER_OF_SYMBOLS_TO_READ = "10000"
    private const val DEFAULT_BUFFER_LENGTH = 100
    private const val CHAR_SIZE = 32

    fun start(args: Array<String>) {
        val commandLine = createCommandLine(args)
        val reader = getReader(commandLine)
        val writer = getWriter(commandLine)
        var numberOfSymbolsToRead = getNumberOfSymbolsToRead(commandLine)

        var string: String? = null
        do {
            string?.let { println(it) }
            string = reader.readLine()
            string?.length?.let { numberOfSymbolsToRead -= it }
        } while (string != null && string.isNotEmpty() && numberOfSymbolsToRead > 0)


    }

    private fun getReader(commandLine: CommandLine): BufferedReader {
        val fileInputStream = FileInputStream(commandLine.getOptionValue("i", DEFAULT_INPUT_FILE_NAME))
        val bufferedInputStream = BufferedInputStream(fileInputStream, CHAR_SIZE * DEFAULT_BUFFER_LENGTH)
        val input = CompressorStreamFactory().createCompressorInputStream(bufferedInputStream)
        return BufferedReader(InputStreamReader(input))
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
}

fun main(args: Array<String>) {
    LabStarter.start(args)
}