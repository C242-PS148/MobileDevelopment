package com.checkmate.data.model.predict

import com.google.gson.annotations.SerializedName

data class PredictAbsen(
    @SerializedName("attendance_count") val attendanceCount: Int,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("predicted_name") val predictedName: String,
    @SerializedName("send_message") val sendMessage: List<String>,
    @SerializedName("status_absen") val statusAbsen: String
)