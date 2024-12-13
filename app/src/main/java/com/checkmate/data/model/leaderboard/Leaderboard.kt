package com.checkmate.data.model.leaderboard

import com.google.gson.annotations.SerializedName

data class Leaderboard(
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("semester_total_point") val semesterTotalPoint: Int,
    @SerializedName("student_id") val studentId: Int,
    @SerializedName("student_name") val studentName: String
)