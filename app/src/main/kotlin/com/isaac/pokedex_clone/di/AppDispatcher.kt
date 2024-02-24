package com.isaac.pokedex_clone.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppDispatcher(val appDispatcher: DispatcherType)

enum class DispatcherType {
  Default,
  IO,
}

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
  @Provides
  @AppDispatcher(DispatcherType.IO)
  fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

  @Provides
  @AppDispatcher(DispatcherType.Default)
  fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
