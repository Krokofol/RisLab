package com.krokofol.lab.config

/**
 * Класс свойств программы.
 * В падлу выносить.
 */
object Properties {

    /**
     * Наименование тега, для которого будет
     * собираться статистика.
     */
    const val NODE_ELEMENT_NAME = "node"

    /**
     * Количество элементов с именем [NODE_ELEMENT_NAME]
     * для обработки.
     */
    const val COUNT_NODES_FOR_READ = 10000000

    /**
     * Наименование файла open-street map.
     * Должен лежать в папке /src/main/resources.
     */
    const val FILE_NAME = "RU-NVS.osm"

    /**
     * Количество соединений к бд.
     */
    const val CONNECTION_POOL_SIZE = 20

    /**
     * Количество записей для исполнения BATCH операции
     */
    const val BATCH_SIZE = 10000
}