package com.krokofol.lab

import java.io.FileNotFoundException
import com.krokofol.lab.config.Properties.FILE_NAME
import com.krokofol.lab.service.OpenStreetMapReaderHandler
import com.krokofol.lab.service.PreloadSchemaService

class Lab1Application

fun main() {
    PreloadSchemaService.preloadSchema()
    Lab1Application::class.java.classLoader.getResourceAsStream(FILE_NAME)
        ?.let { inputStream -> OpenStreetMapReaderHandler.processTheInputStream(inputStream) } ?: throw FileNotFoundException()
}