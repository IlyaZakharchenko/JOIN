package itis.ru.kpfu.join.presentation.model

import itis.ru.kpfu.join.presentation.model.TextMessageModel
import java.io.Serializable

data class DialogModel(
        var dialogName: String? = null,
        var image: Int? = null,
        var lastMessage: TextMessageModel? = null
        ) : Serializable