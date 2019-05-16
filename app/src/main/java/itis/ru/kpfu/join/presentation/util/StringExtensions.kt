package itis.ru.kpfu.join.presentation.util

fun String.getDialogAvatar(): String {
    return "${this[0].toUpperCase()}${this.substringAfter(" ")[0]}".toUpperCase()
}