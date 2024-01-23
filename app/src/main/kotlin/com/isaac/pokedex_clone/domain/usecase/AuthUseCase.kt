package com.isaac.pokedex_clone.domain.usecase

import com.isaac.pokedex_clone.data.model.LoginResponse
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import com.isaac.pokedex_clone.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun login(): ApiResponse<LoginResponse> = authRepository.login()

    suspend fun checkAuth() = authRepository.checkAuth()
}