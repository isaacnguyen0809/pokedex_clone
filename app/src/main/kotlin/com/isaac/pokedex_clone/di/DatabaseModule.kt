package com.isaac.pokedex_clone.di

import android.content.Context
import com.isaac.pokedex_clone.data.local.PokemonDatabase
import com.isaac.pokedex_clone.data.local.dao.FavouritePokemonDao
import com.isaac.pokedex_clone.utils.AppDispatcher
import com.isaac.pokedex_clone.utils.DispatcherType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {
    companion object {
        @Provides
        @Singleton
        fun providePokeAppDatabase(
            @ApplicationContext applicationContext: Context,
            @AppDispatcher(DispatcherType.IO) ioDispatcher: CoroutineDispatcher,
        ) = PokemonDatabase.getInstance(
            context = applicationContext,
            queryExecutor = ioDispatcher.asExecutor(),
        )

        @Provides
        fun provideFavouritePokemonDao(pokemonDatabase: PokemonDatabase): FavouritePokemonDao =
            pokemonDatabase.favoritePokemonDao()
    }
}