package com.checkmate.data.model.predict

import com.google.gson.annotations.SerializedName

data class PredictMood(
    @SerializedName("class_probabilities") val classProbabilities: ClassProbabilities,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("predicted_class_name") val predictedClassName: String
)