package itis.ru.kpfu.join.presentation.model

data class RestorePassFormModel(
        val email: String,
        val password: String,
        val code: String
)