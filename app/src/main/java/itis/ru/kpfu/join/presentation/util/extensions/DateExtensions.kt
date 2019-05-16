package itis.ru.kpfu.join.presentation.util.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toPresentationDate(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(this)
}

fun Date.toPresentationHourMinute(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(this)
}