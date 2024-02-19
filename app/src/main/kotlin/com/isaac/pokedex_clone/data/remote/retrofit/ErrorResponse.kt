package com.isaac.pokedex_clone.data.remote.retrofit

import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "message") val message: String,
)
