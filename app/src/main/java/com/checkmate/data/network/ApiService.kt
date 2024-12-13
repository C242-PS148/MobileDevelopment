package com.checkmate.data.network

import com.checkmate.data.model.MessageResponse
import com.checkmate.data.model.leaderboard.LeaderboardResponse
import com.checkmate.data.model.login.LoginRequest
import com.checkmate.data.model.login.LoginResponse
import com.checkmate.data.model.predict.PredictResponse
import com.checkmate.data.model.profile.ProfileResponse
import com.checkmate.data.model.studentrecap.attendance.AttendanceStatusResponse
import com.checkmate.data.model.studentrecap.mood.MoodStatusResponse
import com.checkmate.data.model.studentrecap.tie.TieStatusResponse
import com.checkmate.data.model.teacherrecap.attendance.TeacherAttendanceStatusResponse
import com.checkmate.data.model.teacherrecap.mood.TeacherMoodStatusResponse
import com.checkmate.data.model.teacherrecap.tie.TeacherTieStatusResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("leaderboard")
    fun leaderboard(@Query("sort_order") sortOrder: String? = null): Call<LeaderboardResponse>

    @GET("student-recap/attendance-status")
    fun getAttendanceStatus(@Query("date") date: String): Call<AttendanceStatusResponse>

    @GET("student-recap/mood-status")
    fun getMoodStatus(@Query("date") date: String): Call<MoodStatusResponse>

    @GET("student-recap/tie-status")
    fun getTieStatus(@Query("date") date: String): Call<TieStatusResponse>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @Multipart
    @POST("register")
    fun register(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("student_number") studentNumber: RequestBody,
        @Part("password") password: RequestBody,
        @Part("role") role: RequestBody,
        @Part profileImage: MultipartBody.Part
    ): Call<MessageResponse>

    @GET("get_profile")
    fun profile(): Call<ProfileResponse>


    //teacher
    @GET("api/attendance-status")
    fun getTeacherAttendanceStatus(): Call<TeacherAttendanceStatusResponse>

    @GET("api/mood-status")
    fun getTeacherMoodStatus(): Call<TeacherMoodStatusResponse>

    @Multipart
    @POST("predict-all")
    fun predict(@Part image: MultipartBody.Part):Call<PredictResponse>

    @GET("api/tie-status")
    fun getTeacherTieStatus(): Call<TeacherTieStatusResponse>
}