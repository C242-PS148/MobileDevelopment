package com.checkmate.data.model.profile

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("class") val className: String?,
    @SerializedName("email") val email: String,
    @SerializedName("grade") val grade: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("role") val role: String,
    @SerializedName("student_number") val studentNumber: String?,
    @SerializedName("username") val username: String,
    @SerializedName("profile_image") val profileImageUrl: String
)