package com.isaac.pokedex_clone.domain.repository

import com.isaac.pokedex_clone.data.model.ListPokemonResponse

interface PokemonRepository {
    suspend fun fetchPokemonList(limit: Int, offset: Int): Result<ListPokemonResponse>
}