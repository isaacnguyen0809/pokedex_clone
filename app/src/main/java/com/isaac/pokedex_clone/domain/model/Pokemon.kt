package com.isaac.pokedex_clone.domain.model

data class Pokemon(
    val name: String,
    val url: String,
    val type: List<String>,
)