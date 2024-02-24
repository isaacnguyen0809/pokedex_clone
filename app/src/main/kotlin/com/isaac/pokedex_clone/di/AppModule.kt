package com.isaac.pokedex_clone.di

import android.content.Context
import com.isaac.pokedex_clone.common.eventbus.EventBus
import com.isaac.pokedex_clone.utils.NetworkConnectionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideEventBus(): EventBus = EventBus()

    @Singleton
    @Provides
    fun provideNetworkConnectionManager(
        @ApplicationContext context: Context,
        eventBus: EventBus
    ): NetworkConnectionManager = NetworkConnectionManager(context, eventBus)
}