package com.checkmate.data.model.predict

import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @SerializedName("results") val predictResults: PredictResults,
    @SerializedName("status") val status: String
)