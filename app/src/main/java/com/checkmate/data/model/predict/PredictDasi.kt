package com.checkmate.data.model.predict

import com.google.gson.annotations.SerializedName

data class PredictDasi(
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("predicted_class") val predictedClass: String
)