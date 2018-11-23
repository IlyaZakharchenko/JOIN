package itis.ru.kpfu.join.db.entity

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class User(
        @PrimaryKey
        var id: Long? = null,
        var username: String? = null,
        var email: String? = null,
        var name: String? = null,
        var lastname: String? = null,
        var phoneNumber: String? = null,
        var imagePath: String? = null,
        var token: String? = null,
        var code: String? = null,
        var password: String? = null,
        var specializations: RealmList<Specialization>? = null
) : RealmObject(), Serializable {}