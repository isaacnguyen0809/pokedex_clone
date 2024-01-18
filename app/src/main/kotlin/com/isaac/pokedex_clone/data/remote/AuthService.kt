package com.isaac.pokedex_clone.data.remote

import com.isaac.pokedex_clone.data.model.LoginResponse
import com.isaac.pokedex_clone.data.remote.body.LoginBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @GET("check-auth")
    suspend fun checkAuth()
}