package itis.ru.kpfu.join.presentation.model

import itis.ru.kpfu.join.db.entity.User
import java.io.Serializable
import java.util.*

data class DialogModel(
        var dialogId: String? = null,
        var username: String? = null,
        var lastMessage: String? = null,
        var lastMessageDate: Date? = null,
        var lastMessageFrom: MessageFromType? = null
) : Serializable

object DialogModelMapper {
    fun map(message: MessageModel, user: User, dialogId: String): DialogModel {
        return DialogModel(
                dialogId,
                if (user.name?.isNotEmpty() == true && user.lastname?.isNotEmpty() == true)
                    "${user.name} ${user.lastname}"
                else
                    user.username,
                message.content,
                message.createdDate,
                message.messageFrom
        )
    }
}