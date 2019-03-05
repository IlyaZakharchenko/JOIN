package itis.ru.kpfu.join.network.pojo

import io.realm.RealmObject
import java.io.Serializable

data class Dialog(
        var dialogName: String? = null,
        var image: Int? = null,
        var lastMessage: TextMessage? = null
        ) : Serializable