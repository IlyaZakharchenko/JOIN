package itis.ru.kpfu.join.db.entity

import io.realm.RealmObject
import java.io.Serializable

open class Dialog(
        var dialogName: String? = null,
        var image: Int? = null,
        var lastMessage: TextMessage? = null
        ) : RealmObject(), Serializable {
}