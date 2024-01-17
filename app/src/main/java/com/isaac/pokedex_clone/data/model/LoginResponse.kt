package com.isaac.pokedex_clone.data.model

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "token") val token :String,
    @Json(name = "refreshToken") val refreshToken :String,
    @Json(name = "id") val id :String,
    @Json(name = "username") val username:String,
)
