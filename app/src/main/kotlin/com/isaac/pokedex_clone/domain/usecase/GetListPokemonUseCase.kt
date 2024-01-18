package com.isaac.pokedex_clone.domain.usecase

import com.isaac.pokedex_clone.data.model.ListPokemonResponse
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import javax.inject.Inject

class GetListPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): Result<ListPokemonResponse> =
        pokemonRepository.fetchPokemonList(
            limit = limit,
            offset = offset,
        )
}