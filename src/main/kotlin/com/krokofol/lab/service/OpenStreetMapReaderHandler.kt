package com.krokofol.lab.service

import java.io.InputStream
import javax.xml.stream.XMLEventReader
import com.krokofol.lab.Node
import com.krokofol.lab.config.Properties.COUNT_NODES_FOR_READ
import com.krokofol.lab.dao.NodeDAO
import com.krokofol.lab.extensions.collectStatistic
import com.krokofol.lab.statistic.StatisticMap

object OpenStreetMapReaderHandler {

    fun processTheInputStream(inputStream: InputStream) {
        val partialUnmarshallerService = PartialUnmarshallerService(Node::class.java, inputStream)
        DataInserterService.insertDataFromIterable(partialUnmarshallerService)
        NodeDAO.closeConnection()
    }
}