package itis.ru.kpfu.join.presentation.model

data class ProjectMemberModel(
        var id: Long? = null,
        var username: String? = null,
        var email: String? = null,
        var profileImage: String? = null,
        var status: Int? = null,
        var isLeader: Boolean? = false
)