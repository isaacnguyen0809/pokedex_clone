package com.isaac.pokedex_clone.di

import com.isaac.pokedex_clone.core.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @AppDispatcher(DispatcherType.Default) defaultDispatcher: CoroutineDispatcher,
    ): CoroutineScope {
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
        }
        return CoroutineScope(SupervisorJob() + defaultDispatcher + errorHandler)
    }

}
