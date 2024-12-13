package com.checkmate.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(token: String?=null): ApiService {
            val client = if (token != null) {
                val authInterceptor = Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(request)
                }
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()
            } else {
                OkHttpClient.Builder()
                    .build()
            }
            val retrofit = Retrofit.Builder()
                .baseUrl("https://checkmate-506488875993.asia-southeast2.run.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
