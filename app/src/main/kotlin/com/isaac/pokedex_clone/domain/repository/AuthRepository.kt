package com.isaac.pokedex_clone.domain.repository

import com.isaac.pokedex_clone.data.model.LoginResponse

interface AuthRepository {
    suspend fun login(): Result<LoginResponse>

    suspend fun logout()

    suspend fun checkAuth(): Result<Any>

}