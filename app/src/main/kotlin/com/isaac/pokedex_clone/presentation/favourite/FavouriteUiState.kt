package com.isaac.pokedex_clone.presentation.favorite_screen

import com.isaac.pokedex_clone.data.mapper.Pokemon

sealed interface FavoriteUiState {

    data object Loading : FavoriteUiState

    data class Success(
        val data: List<Pokemon>,
    ) : FavoriteUiState

    data class Error(val error: Throwable) : FavoriteUiState
}