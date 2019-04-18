package itis.ru.kpfu.join.presentation.util.exceptionprocessor

import android.content.Context
import android.support.annotation.StringRes
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.data.network.exception.*
import itis.ru.kpfu.join.data.network.exception.domain.*

class ExceptionProcessorImpl(val context: Context) : ExceptionProcessor {

    override fun processException(t: Throwable): String {
        return when (t) {
            is IncorrectDataException -> getString(R.string.exception_incorrect_data)
            is NoInternetConnectionException -> getString(R.string.exception_no_connection)
            is TimeOutException -> getString(R.string.exception_time_out)
            is SignInException -> getString(R.string.exception_sign_in)
            is SignUpEmailAlreadyExistsException -> getString(R.string.exception_email_already_exists)
            is SignUpUsernameAlreadyExistsException -> getString(R.string.exception_username_already_exists)
            is InvalidCodeException -> getString(R.string.exception_invalid_code)
            is UserWithThisEmailNotExistsException -> getString(R.string.exception_user_with_this_email_not_exists)
            else -> getString(R.string.exception_unexpected)
        }
    }

    private fun getString(@StringRes text: Int): String {
        return context.getString(text)
    }
}