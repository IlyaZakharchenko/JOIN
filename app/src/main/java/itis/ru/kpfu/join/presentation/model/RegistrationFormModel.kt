package itis.ru.kpfu.join.presentation.model

import java.io.Serializable

data class RegistrationFormModel(
        var username: String? = null,
        var email: String? = null,
        var password: String? = null,
        var passwordAgain: String? = null,
        var code: String? = null): Serializable
