package itis.ru.kpfu.join.presentation.model

import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.presentation.model.ProjectModel
import java.util.Date

data class NotificationModel(
        val id: Long? = null,
        val type: Int? = null,
        val user: User? = null,
        val project: ProjectModel? = null,
        val date: Date? = null
)