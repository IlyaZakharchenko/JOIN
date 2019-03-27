package itis.ru.kpfu.join.data.network.pojo

import com.google.gson.annotations.SerializedName

data class ProfileImage(
        @SerializedName("profileImage")
        val url: String? = null)