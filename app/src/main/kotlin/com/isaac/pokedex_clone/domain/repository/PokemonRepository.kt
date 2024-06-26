package com.isaac.pokedex_clone.domain.repository

import com.isaac.pokedex_clone.data.model.ListPokemonResponse
import com.isaac.pokedex_clone.data.model.PokemonInfo
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse

interface PokemonRepository {
    suspend fun fetchPokemonList(page:Int): ApiResponse<ListPokemonResponse>

    suspend fun fetchPokemonInfo(name : String) : ApiResponse<PokemonInfo>
}