package itis.ru.kpfu.join.presentation.model

import itis.ru.kpfu.join.db.entity.Specialization
import java.io.Serializable

data class ProjectModel(
        var id: Long? = null,
        var name: String? = null,
        var leader: ProjectMemberModel? = null,
        var description: String? = null,
        var userId: Long? = null,
        var participants: List<ProjectMemberModel>? = null,
        var vacancies: List<Specialization>? = null,
        var status: Int? = null) : Serializable