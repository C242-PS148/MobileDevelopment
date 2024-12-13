package com.checkmate.ui.teacher.camera.cameraresult

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.checkmate.data.model.MessageResponse
import com.checkmate.data.model.login.LoginRequest
import com.checkmate.data.model.login.LoginResponse
import com.checkmate.data.model.predict.PredictResponse
import com.checkmate.data.model.predict.PredictResults
import com.checkmate.data.network.ApiConfig
import com.checkmate.utils.SharedPrefUtils
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CameraViewModel : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _predictResults = MutableLiveData<PredictResults>()
    val predictResults: LiveData<PredictResults> = _predictResults

    fun predict(token:String, file: File) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).predict(
            MultipartBody.Part.createFormData(
                "file",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
        )
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { predictResponse ->
                        _predictResults.value = predictResponse.predictResults
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

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}