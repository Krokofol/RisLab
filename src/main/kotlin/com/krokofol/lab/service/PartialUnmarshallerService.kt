package com.krokofol.lab.service

import java.io.InputStream
import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamReader

class PartialUnmarshallerService<T>(
    private val targetClass: Class<T>,
    inputStream: InputStream,
) : AutoCloseable, Iterable<T>, Iterator<T> {

    private val unmarshaller: Unmarshaller = JAXBContext.newInstance(targetClass).createUnmarshaller()

    private val reader: XMLStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream)

    private var endOfNodes = false


    override fun close() {
        reader.close()
    }

    override fun iterator(): Iterator<T> {
        return this
    }

    override fun hasNext(): Boolean {
        return runCatching {
            endOfNodes = skipElements()
            !endOfNodes && reader.hasNext()
        }.onFailure { exception ->
            exception.printStackTrace()
        }.getOrThrow()
    }

    private fun skipElements(): Boolean {
        var isNotNode = false
        var eventType = reader.eventType
        while (eventType.eventTypeIsNodeWithReader(reader)) {
            if (eventType.isStartElement()) {
                reader.localName
                    .let { name -> isNotNode = name == "way" || name == "relation" }
                if (isNotNode)
                    break
            }
            eventType = reader.next()
        }
        return isNotNode
    }

    override fun next(): T {
        if (!hasNext())
            throw NoSuchElementException()
        return runCatching {
            unmarshaller.unmarshal(reader, targetClass).value
        }.onFailure {
            it.printStackTrace()
        }.getOrThrow()
    }

}

private fun Int.eventTypeIsNodeWithReader(reader: XMLStreamReader): Boolean {
    return (this != XMLStreamConstants.END_DOCUMENT
            && this != XMLStreamConstants.START_ELEMENT
            || this == XMLStreamConstants.START_ELEMENT && "node" != reader.localName)
}

private fun Int.isStartElement(): Boolean {
    return this == XMLStreamConstants.START_ELEMENT
}