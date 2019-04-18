package itis.ru.kpfu.join.presentation.model

import itis.ru.kpfu.join.R
import org.json.JSONObject
import java.io.Serializable

data class PushModel(
        val type: PushCategoriesType,
        val projectName: String,
        val userName: String
) : Serializable

object PushModelMapper {
    fun map(data: Map<String, String>): PushModel {
        val projectName = JSONObject(data["project"]).getString("name").orEmpty()
        val username = JSONObject(data["project"]).getJSONObject("leader").getString("username")
        val type = data["type"]?.toInt()

        val enumType = getPushCategoriesType(type ?: -1)

        return PushModel(enumType, projectName, username)
    }
}

enum class PushCategoriesType() {
    ADD_TO_PROJECT_INVITE,
    ADD_TO_PROJECT_ACCEPT,
    ADD_TO_PROJECT_REFUSE,
    JOIN_TO_PROJECT_INVITE,
    JOIN_TO_PROJECT_ACCEPT,
    JOIN_TO_PROJECT_REFUSE,
    REMOVE_FROM_PROJECT,
    UNKNOWN
}

private fun getPushCategoriesType(type: Int): PushCategoriesType {
    return when (type) {
        0 -> PushCategoriesType.ADD_TO_PROJECT_INVITE
        1 -> PushCategoriesType.ADD_TO_PROJECT_ACCEPT
        2 -> PushCategoriesType.JOIN_TO_PROJECT_INVITE
        8 -> PushCategoriesType.JOIN_TO_PROJECT_ACCEPT
        4 -> PushCategoriesType.ADD_TO_PROJECT_REFUSE
        9 -> PushCategoriesType.JOIN_TO_PROJECT_REFUSE
        10 -> PushCategoriesType.REMOVE_FROM_PROJECT
        else -> PushCategoriesType.UNKNOWN
    }
}