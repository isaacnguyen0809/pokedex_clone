package com.isaac.pokedex_clone.common.eventbus

sealed interface EventType {
    data class EventLoginSuccess(
        val userID: String? = "",
    ) : EventType

    data class EventNetworkStatus(
        val isOnline: Boolean,
    ) : EventType
}

