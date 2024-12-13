package com.checkmate.data.model.predict

import com.google.gson.annotations.SerializedName

data class ClassProbabilities(
    @SerializedName("angry") val angry: Double,
    @SerializedName("happy") val happy: Double,
    @SerializedName("neutral") val neutral: Double,
    @SerializedName("sad") val sad: Double
)