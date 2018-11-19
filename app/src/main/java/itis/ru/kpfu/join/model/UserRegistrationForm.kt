package itis.ru.kpfu.join.model

import java.io.Serializable

data class UserRegistrationForm(
        var username: String = "",
        var email: String = "",
        var password: String = "",
        var passwordAgain: String = "",
        var code: String = ""): Serializable
