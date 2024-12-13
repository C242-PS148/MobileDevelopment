package com.checkmate.ui.teacher.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.checkmate.data.model.MessageResponse
import com.checkmate.data.model.teacherrecap.attendance.TeacherAttendanceStatusResponse
import com.checkmate.data.model.teacherrecap.mood.TeacherMoodStatusResponse
import com.checkmate.data.model.teacherrecap.tie.TeacherTieStatusResponse
import com.checkmate.data.network.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeTeahcerViewModel : ViewModel(){
    private val _isLoadingMood = MutableLiveData<Boolean>()
    val isLoadingMood: LiveData<Boolean> = _isLoadingMood

    private val _errorMessageMood = MutableLiveData<String>()
    val errorMessageMood: LiveData<String> = _errorMessageMood

    private val _moodPoint = MutableLiveData<Int>()
    val moodPoint: LiveData<Int> = _moodPoint


    private val _isLoadingTie = MutableLiveData<Boolean>()
    val isLoadingTie: LiveData<Boolean> = _isLoadingTie

    private val _errorMessageTie = MutableLiveData<String>()
    val errorMessageTie: LiveData<String> = _errorMessageTie

    private val _tiePoint = MutableLiveData<Int>()
    val tiePoint: LiveData<Int> = _tiePoint


    private val _isLoadingAttendance = MutableLiveData<Boolean>()
    val isLoadingAttendance: LiveData<Boolean> = _isLoadingAttendance

    private val _errorMessageAttendance = MutableLiveData<String>()
    val errorMessageAttendance: LiveData<String> = _errorMessageAttendance

    private val _attendancePoint = MutableLiveData<Int>()
    val attendancePoint: LiveData<Int> = _attendancePoint


    fun loadAll(token:String){
        loadMood(token)
        loadTie(token)
        loadAttendance(token)
    }

    private fun loadAttendance(token: String) {
        _isLoadingAttendance.value = true
        val client = ApiConfig.getApiService(token).getTeacherAttendanceStatus()
        client.enqueue(object : Callback<TeacherAttendanceStatusResponse> {
            override fun onResponse(
                call: Call<TeacherAttendanceStatusResponse>,
                response: Response<TeacherAttendanceStatusResponse>
            ) {
                _isLoadingAttendance.value = false
                if (response.isSuccessful) {
                    response.body()?.let { attendanceResponse ->
                        _attendancePoint.value = attendanceResponse.debugInfo.totalRecords
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

            override fun onFailure(call: Call<TeacherAttendanceStatusResponse>, t: Throwable) {
                _isLoadingAttendance.value = false
            }
        })
    }

    private fun loadTie(token: String) {
        _isLoadingTie.value = true
        val client = ApiConfig.getApiService(token).getTeacherTieStatus()
        client.enqueue(object : Callback<TeacherTieStatusResponse> {
            override fun onResponse(
                call: Call<TeacherTieStatusResponse>,
                response: Response<TeacherTieStatusResponse>
            ) {
                _isLoadingTie.value = false
                if (response.isSuccessful) {
                    response.body()?.let { tieResponse ->
                        _tiePoint.value = tieResponse.debugInfo.totalRecords
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

            override fun onFailure(call: Call<TeacherTieStatusResponse>, t: Throwable) {
                _isLoadingTie.value = false
            }
        })
    }

    private fun loadMood(token: String) {
        _isLoadingMood.value = true
        val client = ApiConfig.getApiService(token).getTeacherMoodStatus()
        client.enqueue(object : Callback<TeacherMoodStatusResponse> {
            override fun onResponse(
                call: Call<TeacherMoodStatusResponse>,
                response: Response<TeacherMoodStatusResponse>
            ) {
                _isLoadingMood.value = false
                if (response.isSuccessful) {
                    response.body()?.let { moodResponse ->
                        _moodPoint.value = moodResponse.debugInfo.totalRecords
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

            override fun onFailure(call: Call<TeacherMoodStatusResponse>, t: Throwable) {
                _isLoadingMood.value = false
            }
        })
    }
}