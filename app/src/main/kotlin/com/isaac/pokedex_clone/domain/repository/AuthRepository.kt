package com.isaac.pokedex_clone.domain.repository

import com.isaac.pokedex_clone.data.model.LoginResponse
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse

interface AuthRepository {
    suspend fun login(): ApiResponse<LoginResponse>

    suspend fun checkAuth(): Result<Any>

}