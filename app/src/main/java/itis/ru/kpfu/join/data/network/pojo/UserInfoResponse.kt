package itis.ru.kpfu.join.data.network.pojo

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
        @field:SerializedName("user_id")
        val userId: Long,
        @field:SerializedName("Authorization")
        val token: String
)