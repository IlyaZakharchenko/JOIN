package itis.ru.kpfu.join.utils

fun divideString(text: String): HashSet<String> {
    val result = HashSet<String>()
    var word = ""
    text.forEach { c ->
        word += c
        if (c == ',') {
            result.add(word.dropLast(1).trim())
            word = ""
        }
    }

    return result
}

fun transformToString(item: HashSet<String>): String {
    var result = "";
    item.forEach { result += "${it.trim()}, " }

    return result
}