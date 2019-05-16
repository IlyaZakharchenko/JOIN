package itis.ru.kpfu.join.presentation.model

import io.realm.RealmObject
import itis.ru.kpfu.join.data.network.firebase.pojo.response.MessageItem
import java.io.Serializable
import java.util.*

data class MessageModel(
        val content: String? = null,
        val createdDate: Date? = null,
        val messageFrom: MessageFromType
) : Serializable

enum class MessageFromType {
    ME, OTHER
}

object MessageModelMapper {
    fun map(messageItem: MessageItem, userId: Long): MessageModel {
        return MessageModel(
                messageItem.content,
                Date(messageItem.created_date ?: 0L),
                if (messageItem.from == userId.toString()) MessageFromType.ME else MessageFromType.OTHER
        )
    }
}