package com.isaac.pokedex_clone.di

import com.isaac.pokedex_clone.data.repository.PokemonRepositoryImpl
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindPokemonRepository(repositoryImpl: PokemonRepositoryImpl): PokemonRepository

}