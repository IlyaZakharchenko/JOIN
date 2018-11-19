package itis.ru.kpfu.join.db.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Specialization(
        @PrimaryKey
        var id: Long? = null,
        var specializationName: String? = null,
        var knowledgeLevel: String? = null,
        var experience: Int = 0,
        var technologies: String? = null) : RealmObject(), Serializable {}