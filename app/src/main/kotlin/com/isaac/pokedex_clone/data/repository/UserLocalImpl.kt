package com.isaac.pokedex_clone.data.repository

import androidx.datastore.core.DataStore
import com.isaac.pokedex.clone.UserLocal
import com.isaac.pokedex_clone.domain.repository.UserLocalRepository
import com.isaac.pokedex_clone.utils.AppDispatcher
import com.isaac.pokedex_clone.utils.DispatcherType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class UserLocalImpl @Inject constructor(
    private val dataStore: DataStore<UserLocal>,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) : UserLocalRepository {

    override suspend fun update(transform: suspend (current: UserLocal?) -> UserLocal?) = withContext(ioDispatcher) {
        dataStore.updateData { current ->
            transform(current.takeIf { it != UserLocal.getDefaultInstance() }) ?: UserLocal.getDefaultInstance()
        }
    }

    override fun getUserLocal(): Flow<UserLocal?> {
        return dataStore.data.onEach {
        }.map { userLocal ->
            userLocal.takeIf {
                it != UserLocal.getDefaultInstance()
            }
        }.catch { cause: Throwable ->
            if (cause is IOException) {
                emit(null)
            } else {
                throw cause
            }
        }.flowOn(ioDispatcher)
    }

}