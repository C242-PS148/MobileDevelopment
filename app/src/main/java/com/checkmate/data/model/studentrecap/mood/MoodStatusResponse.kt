package com.checkmate.data.model.studentrecap.mood

import com.google.gson.annotations.SerializedName

data class MoodStatusResponse(
    @SerializedName("mood_data") val moodData: MoodData,
    @SerializedName("status") val status: String
)