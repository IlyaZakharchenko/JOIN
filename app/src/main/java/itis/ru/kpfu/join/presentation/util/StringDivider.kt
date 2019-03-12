package itis.ru.kpfu.join.presentation.util

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
    if(word.isNotEmpty()) result.add(word.trim())
    return result
}

fun transformToString(item: HashSet<String>): String {
    var result = "";
    item.forEach { result += "${it.trim()}, " }
    result = result.dropLast(2)

    return result
}