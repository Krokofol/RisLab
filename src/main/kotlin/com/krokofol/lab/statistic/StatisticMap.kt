package com.krokofol.lab.statistic

/**
 * Map для хранения статистики пользователей.
 * Ключ - username пользователя.
 * Значение - количество правок пользователя.
 */
object StatisticMap : HashMap<String, Int>() {

    override fun toString(): String {
        return buildString {
            StatisticMap.forEach { (key, value) ->
                append("$key:$value \n")
            }
        }
    }
}
