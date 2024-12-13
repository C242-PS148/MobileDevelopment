package com.checkmate.data.model.teacherrecap.attendance

import com.google.gson.annotations.SerializedName

data class TeacherAttendanceStatusResponse(
    @SerializedName("attendance_status_data") val attendanceStatusData: List<TeacherAttendanceData>,
    @SerializedName("debug_info") val debugInfo: TeacherDebugInfo,
    @SerializedName("status") val status: String
)