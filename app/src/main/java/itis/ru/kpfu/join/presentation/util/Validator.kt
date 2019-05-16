package itis.ru.kpfu.join.presentation.util

object Validator {

    fun isEmailValid(email: String?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String?): Boolean {
        return password?.length ?: 0 >= 5
    }
}