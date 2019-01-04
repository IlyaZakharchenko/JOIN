package itis.ru.kpfu.join.api.model

data class ProjectMember(
        var id: Long? = null,
        var username: String? = null,
        var email: String? = null,
        var profileImage: String? = null,
        var inviteIsAvailable: Boolean? = true
)