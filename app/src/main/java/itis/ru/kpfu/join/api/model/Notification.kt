package itis.ru.kpfu.join.api.model

import itis.ru.kpfu.join.db.entity.User
import java.util.Date

data class Notification(
        val id: Long? = null,
        val type: Int? = null,
        val user: User? = null,
        val project: Project? = null,
        val date: Date? = null
)