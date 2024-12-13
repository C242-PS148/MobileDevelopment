package com.checkmate.data.model.studentrecap.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceStatusResponse(
    @SerializedName("attendance_data") val attendanceData: AttendanceData,
    @SerializedName("status") val status: String
)