package com.checkmate.data.model.teacherrecap.tie

import com.google.gson.annotations.SerializedName

data class TeacherTieStatusResponse(
    @SerializedName("debug_info") val debugInfo: TeacherDebugInfo,
    @SerializedName("status") val status: String,
    @SerializedName("tie_status_data") val tieStatusData: List<TeacherTieStatusData>
)