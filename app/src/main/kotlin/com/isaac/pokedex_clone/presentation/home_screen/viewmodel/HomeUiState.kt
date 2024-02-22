package com.isaac.pokedex_clone.presentation.home_screen.viewmodel

import com.isaac.pokedex_clone.data.model.PokemonResponse

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val data: List<PokemonResponse>) : HomeUiState
    data class Error(val error: Throwable) : HomeUiState
}