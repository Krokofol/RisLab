package com.krokofol.lab

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options

object LabStarter {

    fun start(args: Array<String>) {
        val commandLine = createCommandLine(args)

        val inputPath = commandLine.getOptionValue("i", "input.bz2")
        val outputPath = commandLine.getOptionValue("o", "output.xml")


    }

    fun createCommandLine(args: Array<String>) : CommandLine {
        val options = Options()
        options.addOption("i", "input", true, "input file path")
        options.addOption("o", "output", true, "output file path")
        val commandLineParser = DefaultParser()
        return commandLineParser.parse(options, args)
    }
}

fun main(args: Array<String>) {

}