package com.checkmate.data.model.teacherrecap.attendance

import com.google.gson.annotations.SerializedName

data class TeacherAttendanceData(
    @SerializedName("date") val date: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("point") val point: Int,
    @SerializedName("semester_total_point") val semesterTotalPoint: Int,
    @SerializedName("status_absen") val statusAbsen: String,
    @SerializedName("student_id") val studentId: Int,
    @SerializedName("username") val username: String
)
