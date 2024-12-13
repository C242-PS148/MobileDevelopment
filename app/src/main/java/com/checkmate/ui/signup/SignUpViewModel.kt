package com.checkmate.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.checkmate.data.model.MessageResponse
import com.checkmate.data.network.ApiConfig
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SignUpViewModel : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun signUp(username: String, email: String, studentNumber: String, password: String, imageProfile: File) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(
            username.toRequestBody("text/plain".toMediaTypeOrNull()),
            email.toRequestBody("text/plain".toMediaTypeOrNull()),
            studentNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
            password.toRequestBody("text/plain".toMediaTypeOrNull()),
            "siswa".toRequestBody("text/plain".toMediaTypeOrNull()),
            MultipartBody.Part.createFormData(
                "profile_image",
                imageProfile.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), imageProfile)
            )
        )
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { messageResponse ->
                        _message.value = messageResponse.message
                    }
                    _isSuccess.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        try {
                            val errorResponse = Gson().fromJson(it, MessageResponse::class.java)
                            _message.value = errorResponse.message
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}