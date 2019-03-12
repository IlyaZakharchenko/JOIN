package itis.ru.kpfu.join.network.pojo

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
        @field:SerializedName("user_id")
        val userId: Long,
        @field:SerializedName("Bearer Authorization")
        val token: String
)