package com.isaac.pokedex_clone.presentation.favourite_screen

import com.isaac.pokedex_clone.data.mapper.Pokemon

sealed interface FavouriteUiState {

    data object Loading : FavouriteUiState

    data class Success(
        val data: List<Pokemon>,
    ) : FavouriteUiState

    data class Error(val error: Throwable) : FavouriteUiState
}