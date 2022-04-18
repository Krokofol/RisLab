@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package com.krokofol.lab.extensions

import com.sun.xml.internal.stream.events.AttributeImpl
import com.sun.xml.internal.stream.events.StartElementEvent
import javax.xml.stream.events.XMLEvent
import com.krokofol.lab.config.Properties.NODE_ELEMENT_NAME
import com.krokofol.lab.exception.AttributeIsMissingException
import com.krokofol.lab.statistic.StatisticMap

/**
 * Функция, проверяющая, является ли [XMLEvent]
 * стартовым элементом (открывающим тегом) и равно
 * ли название тега входной строке [to].
 *
 * @param to - строка для проверки эквивалентности
 * наименования тега.
 * @return true, если [XMLEvent] является стартовым элементом
 * и имя тега равно [to]. Иначе false.
 */
fun XMLEvent.isStartElementAndNameEqualsTo(to: String): Boolean {
    if (this.isStartElement && (this as StartElementEvent).nameAsString() == to)
        return true
    return false
}

/**
 * Функция, обновляющая статистику по данному
 * [XMLEvent] в [map], в которой хранится статистика.
 */
fun XMLEvent.writeEventStatisticInMap() {
    (this as StartElementEvent)
        .let { startElementEvent ->
            val userName = startElementEvent.getUserNameAttribute()
                ?: throw AttributeIsMissingException(
                    "Username attribute is missing"
                )
            var correctionCount = StatisticMap[userName] ?: 1
            StatisticMap.put(userName, ++correctionCount)
        }
}

/**
 * Обновляет информацию в [StatisticMap] по
 * данному [XMLEvent].
 */
fun XMLEvent.collectStatistic() {
    takeIf { event ->
        event.isStartElementAndNameEqualsTo(NODE_ELEMENT_NAME)
    }?.apply {
        writeEventStatisticInMap()
    }
}


/**
 * Наименование аттрибута с логином пользователя.
 */
private const val USER_ATTRIBUTE_NAME = "user"

/**
 * Функция получения из тега [StartElementEvent] значения
 * аттрибута [USER_ATTRIBUTE_NAME].
 * @return строка со значением аттрибута [USER_ATTRIBUTE_NAME]
 * или null, если аттрибут отсутствует.
 */
fun StartElementEvent.getUserNameAttribute(): String? {
    attributes.let { iteratorForAttributes ->
        while (iteratorForAttributes.hasNext()) {
            val currentAttributeValue =
                iteratorForAttributes.next() as AttributeImpl
            if (currentAttributeValue.name.localPart == USER_ATTRIBUTE_NAME) {
                return currentAttributeValue.value
            }
        }
    }
    return null
}
