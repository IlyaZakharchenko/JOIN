package itis.ru.kpfu.join.db.entity

data class Project(
        var id: Long? = null,
        var name: String? = null,
        var leader: User? = null,
        var description: String? = null,
        var participants: MutableList<User>? = null,
        var vacancies: MutableList<Specialization>? = null)