package com.isaac.pokedex_clone.data.model

import com.squareup.moshi.Json

data class RefreshTokenResponse(
  @Json(name = "token") val token: String,
)
