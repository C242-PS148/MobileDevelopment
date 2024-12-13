package com.checkmate.ui.student.information.detailinformation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.checkmate.data.model.MessageResponse
import com.checkmate.data.model.leaderboard.Leaderboard
import com.checkmate.data.model.leaderboard.LeaderboardResponse
import com.checkmate.data.model.studentrecap.attendance.AttendanceStatusResponse
import com.checkmate.data.model.studentrecap.mood.MoodStatusResponse
import com.checkmate.data.model.studentrecap.tie.TieStatusResponse
import com.checkmate.data.network.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailInformationViewModel : ViewModel(){
    private val _isLoadingMood = MutableLiveData<Boolean>()
    val isLoadingMood: LiveData<Boolean> = _isLoadingMood

    private val _errorMessageMood = MutableLiveData<String>()
    val errorMessageMood: LiveData<String> = _errorMessageMood

    private val _moodPoint = MutableLiveData<Int>()
    val moodPoint: LiveData<Int> = _moodPoint

    private val _moodMaxPoint = MutableLiveData<Int>()
    val moodMaxPoint: LiveData<Int> = _moodMaxPoint


    private val _isLoadingTie = MutableLiveData<Boolean>()
    val isLoadingTie: LiveData<Boolean> = _isLoadingTie

    private val _errorMessageTie = MutableLiveData<String>()
    val errorMessageTie: LiveData<String> = _errorMessageTie

    private val _tiePoint = MutableLiveData<Int>()
    val tiePoint: LiveData<Int> = _tiePoint

    private val _tieMaxPoint = MutableLiveData<Int>()
    val tieMaxPoint: LiveData<Int> = _tieMaxPoint


    private val _isLoadingAttendance = MutableLiveData<Boolean>()
    val isLoadingAttendance: LiveData<Boolean> = _isLoadingAttendance

    private val _errorMessageAttendance = MutableLiveData<String>()
    val errorMessageAttendance: LiveData<String> = _errorMessageAttendance

    private val _attendancePoint = MutableLiveData<Int>()
    val attendancePoint: LiveData<Int> = _attendancePoint

    private val _attendanceMaxPoint = MutableLiveData<Int>()
    val attendanceMaxPoint: LiveData<Int> = _attendanceMaxPoint

    fun loadAll(token:String, date:String){
        loadMood(token, date)
        loadTie(token, date)
        loadAttendance(token, date)
    }

    private fun loadAttendance(token: String, date:String) {
        _isLoadingAttendance.value = true
        val client = ApiConfig.getApiService(token).getAttendanceStatus(date)
        client.enqueue(object : Callback<AttendanceStatusResponse> {
            override fun onResponse(
                call: Call<AttendanceStatusResponse>,
                response: Response<AttendanceStatusResponse>
            ) {
                _isLoadingAttendance.value = false
                if (response.isSuccessful) {
                    response.body()?.let { attendanceResponse ->
                        _attendancePoint.value = attendanceResponse.attendanceData.point
                        _attendanceMaxPoint.value = attendanceResponse.attendanceData.semesterTotalPoint
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        try {
                            val errorResponse = Gson().fromJson(it, MessageResponse::class.java)
                            _errorMessageAttendance.value = errorResponse.message
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AttendanceStatusResponse>, t: Throwable) {
                _isLoadingAttendance.value = false
            }
        })
    }

    private fun loadTie(token: String, date:String) {
        _isLoadingTie.value = true
        val client = ApiConfig.getApiService(token).getTieStatus(date)
        client.enqueue(object : Callback<TieStatusResponse> {
            override fun onResponse(
                call: Call<TieStatusResponse>,
                response: Response<TieStatusResponse>
            ) {
                _isLoadingTie.value = false
                if (response.isSuccessful) {
                    response.body()?.let { tieResponse ->
                        _tiePoint.value = tieResponse.tieData.point
                        _tieMaxPoint.value = tieResponse.tieData.semesterTotalPoint
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        try {
                            val errorResponse = Gson().fromJson(it, MessageResponse::class.java)
                            _errorMessageTie.value = errorResponse.message
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TieStatusResponse>, t: Throwable) {
                _isLoadingTie.value = false
            }
        })
    }

    private fun loadMood(token: String, date:String) {
        _isLoadingMood.value = true
        val client = ApiConfig.getApiService(token).getMoodStatus(date)
        client.enqueue(object : Callback<MoodStatusResponse> {
            override fun onResponse(
                call: Call<MoodStatusResponse>,
                response: Response<MoodStatusResponse>
            ) {
                _isLoadingMood.value = false
                if (response.isSuccessful) {
                    response.body()?.let { moodResponse ->
                        _moodPoint.value = moodResponse.moodData.point
                        _moodMaxPoint.value = moodResponse.moodData.semesterTotalPoint
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        try {
                            val errorResponse = Gson().fromJson(it, MessageResponse::class.java)
                            _errorMessageMood.value = errorResponse.message
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MoodStatusResponse>, t: Throwable) {
                _isLoadingMood.value = false
            }
        })
    }
}