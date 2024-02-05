package com.isaac.pokedex_clone.domain.repository

import com.isaac.pokedex.clone.UserLocal
import kotlinx.coroutines.flow.Flow

interface UserLocalRepository {
    suspend fun update(transform: suspend (current: UserLocal?) -> UserLocal?): UserLocal?

    fun getUserLocal(): Flow<UserLocal?>
}