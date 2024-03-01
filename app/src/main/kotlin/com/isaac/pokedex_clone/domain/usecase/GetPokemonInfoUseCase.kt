package com.isaac.pokedex_clone.domain.usecase

import com.isaac.pokedex_clone.data.model.PokemonInfo
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonInfoUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) {
    suspend operator fun invoke(name: String): ApiResponse<PokemonInfo> =
        pokemonRepository.fetchPokemonInfo(name = name)
}