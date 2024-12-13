package com.checkmate.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("user") val user: User
)