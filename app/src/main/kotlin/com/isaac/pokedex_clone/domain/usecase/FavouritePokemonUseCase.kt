package com.isaac.pokedex_clone.domain.usecase

import com.isaac.pokedex_clone.data.mapper.Pokemon
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import javax.inject.Inject

class FavouritePokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) {
    suspend fun getAllListFavouritePokemon(): Result<List<Pokemon>> = pokemonRepository.getAllFavouritePokemon()

    suspend fun likePokemon(pokemon: Pokemon) = pokemonRepository.likePokemon(pokemon)

    suspend fun unlikePokemon(pokemon: Pokemon) = pokemonRepository.unlikePokemon(pokemon)
}