package com.isaac.pokedex_clone.presentation.login_screen

import com.isaac.pokedex_clone.data.model.LoginResponse

sealed interface LoginUiState {
    data object Loading : LoginUiState
    data class Success(val data: LoginResponse) : LoginUiState
    data class Error(val error: Throwable) : LoginUiState
    data object Initial : LoginUiState
}