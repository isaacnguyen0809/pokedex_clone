package com.isaac.pokedex_clone.data.remote.retrofit

import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "error") val error: String,
    @Json(name = "code") val code: Int,
)
