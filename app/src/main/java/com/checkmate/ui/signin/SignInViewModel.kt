package com.checkmate.ui.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.checkmate.data.model.MessageResponse
import com.checkmate.data.model.login.LoginRequest
import com.checkmate.data.model.login.LoginResponse
import com.checkmate.data.network.ApiConfig
import com.checkmate.utils.SharedPrefUtils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _role = MutableLiveData<String>()
    val role: LiveData<String> = _role

    fun signIn(ctx:Context, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(LoginRequest(email,password))
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        SharedPrefUtils.saveAuthToken(ctx, loginResponse.accessToken)
                        _role.value = loginResponse.user.role
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

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}