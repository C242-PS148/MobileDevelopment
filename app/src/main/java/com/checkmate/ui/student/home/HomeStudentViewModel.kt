package com.checkmate.ui.student.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.checkmate.data.model.MessageResponse
import com.checkmate.data.model.leaderboard.Leaderboard
import com.checkmate.data.model.leaderboard.LeaderboardResponse
import com.checkmate.data.network.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeStudentViewModel : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _listLeaderboard = MutableLiveData<List<Leaderboard>>()
    val listLeaderboard: LiveData<List<Leaderboard>> = _listLeaderboard

    fun loadLeaderboard(token: String, isTop: Boolean) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).leaderboard(if(isTop) null else "asc")
        client.enqueue(object : Callback<LeaderboardResponse> {
            override fun onResponse(
                call: Call<LeaderboardResponse>,
                response: Response<LeaderboardResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { leaderboardResponse ->
                       _listLeaderboard.value = leaderboardResponse.listLeaderboard
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        try {
                            val errorResponse = Gson().fromJson(it, MessageResponse::class.java)
                            _errorMessage.value = errorResponse.message
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LeaderboardResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}