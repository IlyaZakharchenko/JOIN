package itis.ru.kpfu.join.presentation.util.exceptionprocessor

import android.content.Context
import android.support.annotation.StringRes
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.network.exception.*

class ExceptionProcessorImpl(val context: Context): ExceptionProcessor {

    override fun processException(t: Throwable): String {
        return when(t) {
            is IncorrectDataException -> getString(R.string.exception_incorrect_data)
            is NoInternetConnectionException -> getString(R.string.exception_no_connection)
            is NotAuthorizedException -> getString(R.string.exception_not_authorized)
            is TimeOutException -> getString(R.string.exception_time_out)
            else -> getString(R.string.exception_unexpected)
        }
    }

    private fun getString(@StringRes text: Int): String {
        return context.getString(text)
    }
}