package itis.ru.kpfu.join.model

data class UserRegistrationForm(val username: String = "",
        val email: String = "",
        val password: String = "",
        val passwordAgain: String = "")
