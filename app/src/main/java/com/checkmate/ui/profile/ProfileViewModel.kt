package com.checkmate.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.checkmate.data.model.MessageResponse
import com.checkmate.data.model.profile.Profile
import com.checkmate.data.model.profile.ProfileResponse
import com.checkmate.data.network.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    fun loadProfile(token: String, isCheckOnly: Boolean) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).profile()
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { profileResponse ->
                       _profile.value = profileResponse.profile
                    }
                } else {
                    if(!isCheckOnly){
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
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}