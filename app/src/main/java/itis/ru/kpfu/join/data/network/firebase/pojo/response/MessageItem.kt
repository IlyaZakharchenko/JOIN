package itis.ru.kpfu.join.data.network.firebase.pojo.response

data class MessageItem(
        var content: String? = null,
        var created_date: Long? = null,
        var from: String? = null
)