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
        var profileImage: String? = null,
        var token: String? = null,
        var password: String? = null,
        var specializations: RealmList<Specialization>? = null
) : RealmObject(), Serializable {

    fun getParsedSpecializations(): MutableList<Specialization> {
        val result = ArrayList<Specialization>()

        specializations?.forEach {
            result.add(Specialization(it.name, it.knowledgeLevel, it.experience, it.technologies))
        }

        return result
    }
}