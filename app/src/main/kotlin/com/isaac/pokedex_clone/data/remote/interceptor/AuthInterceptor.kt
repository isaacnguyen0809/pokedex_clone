package com.isaac.pokedex_clone.data.remote.interceptor

import com.isaac.pokedex_clone.data.remote.AuthService
import com.isaac.pokedex_clone.data.remote.AuthService.Factory.CUSTOM_HEADER
import com.isaac.pokedex_clone.data.remote.AuthService.Factory.NO_AUTH
import com.isaac.pokedex_clone.data.remote.body.RefreshTokenBody
import com.isaac.pokedex_clone.domain.repository.UserLocalRepository
import com.isaac.pokedex_clone.utils.AppDispatcher
import com.isaac.pokedex_clone.utils.DispatcherType
import com.isaac.pokedex_clone.utils.onError
import com.isaac.pokedex_clone.utils.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Provider


class AuthInterceptor @Inject constructor(
    private val userLocalRepository: UserLocalRepository,
    private val authService: Provider<AuthService>,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) : Interceptor {
    private val mutex = Mutex()
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (NO_AUTH in request.headers.values(CUSTOM_HEADER)) {
            return chain.proceedWithToken(request, null)
        }

        val accessToken = runBlocking(ioDispatcher) {
            userLocalRepository.getUserLocal().first()?.token
        }

        val res = chain.proceedWithToken(request, accessToken)
        if (res.code != HttpURLConnection.HTTP_UNAUTHORIZED) {
            return res
        }

        val newToken = runBlocking(ioDispatcher) {
            mutex.withLock {
                val user = userLocalRepository.getUserLocal().first()
                val maybeUpdatedToken = user?.token

                when {
                    user == null || maybeUpdatedToken == null ->
                        null // already logged out!
                    maybeUpdatedToken != accessToken ->
                        maybeUpdatedToken // refreshed by another request
                    else -> {
                        val refreshTokenResponse = authService.get()
                            .refreshToken(
                                RefreshTokenBody(
                                    refreshToken = user.refreshToken,
                                    username = user.username
                                )
                            )
                        var token: String? = null
                        refreshTokenResponse.onSuccess {
                            token = it.token
                            userLocalRepository.update { userLocal ->
                                (userLocal ?: return@update null)
                                    .toBuilder()
                                    .setToken(it.token)
                                    .build()
                            }
                        }.onError { code, _, _ ->
                            if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                userLocalRepository.update {
                                    null
                                }
                            }
                        }
                        return@withLock token
                    }
                }
            }
        }
        return if (newToken !== null) chain.proceedWithToken(request, newToken) else res
    }

    private fun Interceptor.Chain.proceedWithToken(req: Request, token: String?): Response =
        req.newBuilder()
            .apply {
                if (token !== null) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .removeHeader(CUSTOM_HEADER)
            .build()
            .let(::proceed)
}