package com.krokofol.lab.service

import com.krokofol.lab.Node
import com.krokofol.lab.dao.NodeDAO

object DataInserterService {

    fun insertDataFromIterable(nodes: Iterable<Node>) {
        nodes.forEach { node -> NodeDAO.insertNode(node) }
    }
}