package itis.ru.kpfu.join.api.model

import io.realm.RealmObject
import java.io.Serializable

open class TextMessage(
        var text: String? = null,
        var dateSend: String? = null,
        var from: String? = null,
        var to: String? = null) : RealmObject(), Serializable