package itis.ru.kpfu.join.db.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
        @PrimaryKey
        var id: Long? = null,
        var userName: String? = null,
        var email: String? = null,
        var firstName: String? = null,
        var middleName: String? = null,
        var lastName: String? = null,
        var imagePath: String? = null,
        var phone: String? = null

) : RealmObject() {

}