package itis.ru.kpfu.join.presentation.model

data class SignInFormModel(
        val email: String,
        val password: String,
        val tokenDevice: String
)