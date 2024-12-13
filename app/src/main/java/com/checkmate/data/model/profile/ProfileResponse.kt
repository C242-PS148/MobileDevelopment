package com.checkmate.data.model.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("data") val profile: Profile,
    @SerializedName("status") val status: String
)