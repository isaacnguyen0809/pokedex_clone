package com.isaac.pokedex_clone.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//Flow
inline fun <T> Flow<T>.collectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (value: T) -> Unit,
): Job = owner.lifecycleScope.launch {
    owner.lifecycle.repeatOnLifecycle(state = minActiveState) {
        collect { action(it) }
    }
}

//Common interface for one-time event
interface UiEvent<T> {
    val data: T
    val onConsumed: () -> Unit
}

//Collect the state of event and consume the value immediately
inline fun <T> Flow<UiEvent<T>?>.collectEvent(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (value: T?) -> Unit,
): Job = owner.lifecycleScope.launch {
    owner.lifecycle.repeatOnLifecycle(state = minActiveState) {
        collect { uiEvent ->
            action(uiEvent?.data)
            uiEvent?.onConsumed?.invoke()
        }
    }
}