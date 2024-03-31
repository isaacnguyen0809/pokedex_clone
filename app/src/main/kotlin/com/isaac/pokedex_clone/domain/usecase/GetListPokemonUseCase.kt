package com.isaac.pokedex_clone.domain.usecase

import com.isaac.pokedex_clone.data.model.ListPokemonResponse
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import javax.inject.Inject

class GetListPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) {
    suspend operator fun invoke(page: Int): ApiResponse<ListPokemonResponse> =
        pokemonRepository.fetchPokemonList(page)
}