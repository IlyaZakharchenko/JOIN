package itis.ru.kpfu.join.network.pojo

import io.realm.RealmObject
import java.io.Serializable

data class TextMessage(
        var text: String? = null,
        var dateSend: String? = null,
        var from: String? = null,
        var to: String? = null) : Serializable