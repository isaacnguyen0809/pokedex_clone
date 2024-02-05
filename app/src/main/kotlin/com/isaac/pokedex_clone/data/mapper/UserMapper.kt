package com.isaac.pokedex_clone.data.mapper

import com.isaac.pokedex.clone.UserLocal
import com.isaac.pokedex_clone.data.model.LoginResponse

fun LoginResponse.toUserLocal(): UserLocal = UserLocal.newBuilder()
    .setId(id)
    .setUsername(username)
    .setToken(token)
    .setRefreshToken(refreshToken)
    .build()