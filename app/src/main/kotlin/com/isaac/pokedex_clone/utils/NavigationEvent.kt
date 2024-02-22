package com.isaac.pokedex_clone.utils

sealed interface NavigationEvent {
    data object NavigateToLogin : NavigationEvent
}