package com.checkmate.data.model.predict

import com.google.gson.annotations.SerializedName

data class PredictResults(
    @SerializedName("predict_absen") val predictAbsen: PredictAbsen?,
    @SerializedName("predict_dasi") val predictDasi: PredictDasi?,
    @SerializedName("predict_mood") val predictMood: PredictMood?
)