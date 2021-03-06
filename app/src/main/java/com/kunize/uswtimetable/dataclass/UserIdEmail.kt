package com.kunize.uswtimetable.dataclass

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserIdEmail(
    @SerializedName("loginId") val id: String,
    val email: String
): Serializable
