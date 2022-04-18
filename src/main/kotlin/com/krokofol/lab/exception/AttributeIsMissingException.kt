package com.krokofol.lab.exception

/**
 * Класс-исключение, выбрасываемый в случае, когда внутри
 * тега отсутствует аттрибут с необходимым наименованием.
 */
class AttributeIsMissingException(override val message: String?) :
    RuntimeException(message)
