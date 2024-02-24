package com.isaac.pokedex_clone.common.eventbus

import com.isaac.pokedex_clone.PokedexCloneApplication
import com.isaac.pokedex_clone.core.ApplicationScope
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventBus @Inject constructor() {

    private val externalScope by lazy {
        EntryPoints.get(PokedexCloneApplication.gInstance, CoroutineDiInterface::class.java).getExternalScope()
    }

    private val _eventMutableFlow = MutableSharedFlow<EventType>()

    val eventFlow = _eventMutableFlow.asSharedFlow().distinctUntilChanged()

    fun produceEvent(event: EventType) {
        externalScope.launch {
            _eventMutableFlow.emit(event)
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface CoroutineDiInterface {
        @ApplicationScope
        fun getExternalScope(): CoroutineScope
    }
}