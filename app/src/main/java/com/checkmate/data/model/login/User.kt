package com.checkmate.data.model.login

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("student_number") val studentNumber: String?,
    @SerializedName("username") val username: String
)