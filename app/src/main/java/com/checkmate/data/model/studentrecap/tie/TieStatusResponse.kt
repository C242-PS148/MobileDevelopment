package com.checkmate.data.model.studentrecap.tie

import com.google.gson.annotations.SerializedName

data class TieStatusResponse(
    @SerializedName("status") val status: String,
    @SerializedName("tie_data") val tieData: TieData
)