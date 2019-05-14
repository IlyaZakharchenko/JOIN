package itis.ru.kpfu.join.presentation.model

import itis.ru.kpfu.join.db.entity.User
import java.io.Serializable

data class CreatedDialogModel(
        val dialogId: String? = null,
        val companionName: String? = null
): Serializable

object CreatedDialogModelMapper {

    fun map(dialogId: String, user: User): CreatedDialogModel {
        return CreatedDialogModel(
                dialogId,
                if (user.name?.isNotEmpty() == true && user.lastname?.isNotEmpty() == true)
                    "${user.lastname} ${user.name}"
                else user.username
        )
    }
}