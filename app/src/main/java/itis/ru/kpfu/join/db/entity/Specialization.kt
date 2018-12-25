package itis.ru.kpfu.join.db.entity

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Specialization(
        var name: String? = null,
        var knowledgeLevel: Int? = null,
        var experience: Int = 0,
        var technologies: String? = null) : RealmObject(), Serializable