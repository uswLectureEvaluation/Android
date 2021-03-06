package com.kunize.uswtimetable.dataclass

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SignUpFormat(
    @SerializedName("loginId") val id: String,
    val password: String,
    val email: String,
): Serializable
