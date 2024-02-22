package com.isaac.pokedex_clone.data.remote

import com.isaac.pokedex_clone.data.model.LoginResponse
import com.isaac.pokedex_clone.data.model.RefreshTokenResponse
import com.isaac.pokedex_clone.data.remote.body.LoginBody
import com.isaac.pokedex_clone.data.remote.body.RefreshTokenBody
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): ApiResponse<LoginResponse>

    @GET("demo")
    suspend fun callDemo(@Query("count") count: Int): ApiResponse<String>

    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @POST("refresh-token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenBody): ApiResponse<RefreshTokenResponse>

    companion object Factory {
        const val CUSTOM_HEADER = "@"
        const val NO_AUTH = "NoAuth"
    }
}