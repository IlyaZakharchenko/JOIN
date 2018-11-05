package itis.ru.kpfu.join.db.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TestEntity(
        var name: String? = "",
        var site: String? = "",
        var desc: String? = "",
        var link: String? = "",
        @PrimaryKey
        var elementPureHtml: String? = ""

) : RealmObject() {

    override fun toString(): String {
        return "$elementPureHtml"
    }
}