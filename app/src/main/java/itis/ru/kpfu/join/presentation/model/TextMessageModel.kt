package itis.ru.kpfu.join.presentation.model

import io.realm.RealmObject
import java.io.Serializable

data class TextMessageModel(
        var text: String? = null,
        var dateSend: String? = null,
        var from: String? = null,
        var to: String? = null) : Serializable