package com.isaac.pokedex_clone.presentation.home.viewmodel

import com.isaac.pokedex_clone.data.mapper.Pokemon

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data object RefreshList : HomeUiState

    data class Success(
        val data: List<Pokemon?>,
        val currentPage: Int,
        val isLoadingNextPage: Boolean,
    ) : HomeUiState

    data class Error(val error: Throwable) : HomeUiState
}