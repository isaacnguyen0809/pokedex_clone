package com.isaac.pokedex_clone.domain.usecase

import com.isaac.pokedex.clone.UserLocal
import com.isaac.pokedex_clone.data.mapper.toUserLocal
import com.isaac.pokedex_clone.data.model.LoginResponse
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import com.isaac.pokedex_clone.domain.repository.AuthRepository
import com.isaac.pokedex_clone.domain.repository.UserLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userLocalRepository: UserLocalRepository,
) {
    suspend fun login(): ApiResponse<LoginResponse> = authRepository.login()

    suspend fun saveLoginResponseToUserLocal(loginResponse: LoginResponse) {
        userLocalRepository.update {
            loginResponse.toUserLocal()
        }
    }

    fun getUser(): Flow<UserLocal?> {
        return userLocalRepository.getUserLocal()
    }

    suspend fun logout() {
        userLocalRepository.update { null }
    }

    suspend fun callDemo(): ApiResponse<String> = authRepository.callDemo()

    suspend fun checkAuth() = authRepository.checkAuth()
}