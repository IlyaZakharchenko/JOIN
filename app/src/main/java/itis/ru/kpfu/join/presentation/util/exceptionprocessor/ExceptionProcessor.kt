package itis.ru.kpfu.join.presentation.util.exceptionprocessor

interface ExceptionProcessor {

    fun processException(t: Throwable): String
}