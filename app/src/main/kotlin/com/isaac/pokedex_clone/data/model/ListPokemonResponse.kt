package com.isaac.pokedex_clone.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListPokemonResponse(
    @Json(name = "count") val count: Int?,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val result: List<PokemonResponse>,
) : Parcelable