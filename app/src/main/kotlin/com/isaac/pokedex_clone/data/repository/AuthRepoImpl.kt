package com.isaac.pokedex_clone.data.repository

import com.isaac.pokedex_clone.data.model.LoginResponse
import com.isaac.pokedex_clone.data.remote.AuthService
import com.isaac.pokedex_clone.data.remote.body.LoginBody
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import com.isaac.pokedex_clone.domain.repository.AuthRepository
import com.isaac.pokedex_clone.utils.AppDispatcher
import com.isaac.pokedex_clone.utils.DispatcherType
import com.isaac.pokedex_clone.utils.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authService: AuthService,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {
    override suspend fun login(): ApiResponse<LoginResponse> = withContext(ioDispatcher) {
        authService.login(
            LoginBody(
                username = "isaacNguyen",
                password = "123456",
            ),
        )
    }


    override suspend fun logout() {
        runSuspendCatching {
            //TODO : DO NOTHING FOR NOW
        }
    }

    override suspend fun checkAuth(): Result<Any> {
        return runCatching {
            authService.checkAuth()
        }
    }

}
