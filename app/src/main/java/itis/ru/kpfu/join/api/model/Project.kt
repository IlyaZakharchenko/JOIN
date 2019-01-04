package itis.ru.kpfu.join.api.model

import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.db.entity.User
import java.io.Serializable

data class Project(
        var id: Long? = null,
        var name: String? = null,
        var leader: User? = null,
        var description: String? = null,
        var userId: Long? = null,
        var participants: List<ProjectMember>? = null,
        var vacancies: List<Specialization>? = null): Serializable