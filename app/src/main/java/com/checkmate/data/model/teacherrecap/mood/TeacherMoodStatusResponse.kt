package com.checkmate.data.model.teacherrecap.mood

import com.google.gson.annotations.SerializedName

data class TeacherMoodStatusResponse(
    @SerializedName("debug_info") val debugInfo: TeacherDebugInfo,
    @SerializedName("mood_status_data") val moodStatusData: List<TeacherMoodStatusData>,
    @SerializedName("status") val status: String
)