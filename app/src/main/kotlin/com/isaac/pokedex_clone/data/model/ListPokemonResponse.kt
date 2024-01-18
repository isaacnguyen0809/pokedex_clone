package com.isaac.pokedex_clone.data.model

import com.squareup.moshi.Json

data class ListPokemonResponse(
    @Json(name = "count") val count: Int?,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val result: List<PokemonResponse>,
)