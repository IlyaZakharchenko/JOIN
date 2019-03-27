package itis.ru.kpfu.join.data.network.exception.domain

import java.lang.Exception

class SignInException : DomainException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}