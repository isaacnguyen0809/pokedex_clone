package com.isaac.pokedex_clone.data.remote

import com.isaac.pokedex_clone.data.model.LoginResponse
import com.isaac.pokedex_clone.data.remote.body.LoginBody
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): ApiResponse<LoginResponse>

    @GET("check-auth")
    suspend fun checkAuth()

    @GET("demo")
    suspend fun callDemo(@Query("count") count: Int): ApiResponse<String>
}