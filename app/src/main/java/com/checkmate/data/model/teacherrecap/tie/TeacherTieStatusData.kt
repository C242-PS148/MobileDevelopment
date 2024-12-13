package com.checkmate.data.model.teacherrecap.tie

import com.google.gson.annotations.SerializedName

data class TeacherTieStatusData(
    @SerializedName("date") val date: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("point") val point: Int,
    @SerializedName("semester_total_point") val semesterTotalPoint: Int,
    @SerializedName("status_dasi") val statusDasi: String,
    @SerializedName("student_id") val studentId: Int,
    @SerializedName("username") val username: String
)