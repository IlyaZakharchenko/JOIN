package itis.ru.kpfu.join.api.model

import com.google.gson.annotations.SerializedName

data class ProfileImage(
        @SerializedName("profileImage")
        val url: String? = null)