package com.isaac.pokedex_clone.di

import com.isaac.pokedex_clone.data.repository.AuthRepoImpl
import com.isaac.pokedex_clone.data.repository.PokemonRepoImpl
import com.isaac.pokedex_clone.domain.repository.AuthRepository
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindPokemonRepository(repositoryImpl: PokemonRepoImpl): PokemonRepository

    @Binds
    fun bindAuthRepository(repositoryImpl: AuthRepoImpl): AuthRepository

}