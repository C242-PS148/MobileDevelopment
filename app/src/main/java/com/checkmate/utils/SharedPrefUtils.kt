package com.checkmate.utils

import android.content.Context

class SharedPrefUtils {
    companion object{
        fun saveAuthToken(ctx:Context, token:String){
            val sharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("token", token)
            editor.apply()
        }
        fun removeAuthToken(ctx:Context){
            val sharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("token")
            editor.apply()
        }
        fun getAuthToken(ctx:Context):String{
            val sharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", "")?:""
        }
    }
}