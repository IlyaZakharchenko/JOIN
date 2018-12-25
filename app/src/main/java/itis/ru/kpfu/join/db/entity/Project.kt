package itis.ru.kpfu.join.db.entity

data class Project(
        var name: String = "",
        var description: String = "",
        var userList: List<User> = ArrayList())