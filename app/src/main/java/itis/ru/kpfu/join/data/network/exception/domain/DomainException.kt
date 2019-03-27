package itis.ru.kpfu.join.data.network.exception.domain

import java.lang.Exception

open class DomainException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}