package itis.ru.kpfu.join.presentation.util

fun parseLevelFromString(level: String?): Int {
    return when (level) {
        "Junior" -> 0
        "Middle" -> 1
        "Senior" -> 2
        else -> 404
    }
}

fun parseLevelFromInt(level: Int?): String {
    return when (level) {
        0 -> "Junior"
        1 -> "Middle"
        2 -> "Senior"
        else -> "Not Found"
    }
}

