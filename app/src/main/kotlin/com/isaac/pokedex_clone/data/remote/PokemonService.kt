package com.isaac.pokedex_clone.data.remote

import com.isaac.pokedex_clone.data.model.ListPokemonResponse
import com.isaac.pokedex_clone.data.model.PokemonInfo
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): ApiResponse<ListPokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(@Path("name") name: String): ApiResponse<PokemonInfo>

}