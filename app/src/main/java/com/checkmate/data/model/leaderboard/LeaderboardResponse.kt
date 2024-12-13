package com.checkmate.data.model.leaderboard

import com.google.gson.annotations.SerializedName

data class LeaderboardResponse(
    @SerializedName("leaderboard") val listLeaderboard: List<Leaderboard>,
    @SerializedName("status") val status: String
)