package com.isaac.pokedex_clone.utils

import com.isaac.pokedex_clone.data.remote.retrofit.ErrorResponse

sealed interface NavigationEvent {
    data object NavigateToLogin : NavigationEvent
}

sealed interface OneTimeEvent {
    data class Toast(
        val code: Int? = null,
        val message: String? = null,
        val errorBody: ErrorResponse? = null,
    ) :
        OneTimeEvent
}