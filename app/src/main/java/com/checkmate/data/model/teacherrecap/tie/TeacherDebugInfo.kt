package com.checkmate.data.model.teacherrecap.tie

import com.google.gson.annotations.SerializedName

data class TeacherDebugInfo(
    @SerializedName("queried_date") val queriedDate: String,
    @SerializedName("total_records") val totalRecords: Int
)