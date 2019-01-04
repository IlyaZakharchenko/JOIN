package itis.ru.kpfu.join.api.model

import java.util.Date

data class Notification(
        val id: Long? = null,
        val message: String? = null,
        val projectId: Long? = null,
        val userId: Long? = null,
        val type: Int? = null,
        val date: Date? = null,
        val seeing: Boolean? = null
)