package com.isaac.pokedex_clone.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

fun <T> Flow<T>.collectFlowWithLifeCycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    },
    block: suspend (T) -> Unit,
) {
    lifecycle.coroutineScope.launch(exceptionHandler) {
        flowWithLifecycle(lifecycle, minActiveState).collect(block)
    }
}

@JvmOverloads
fun <T> Flow<T>.collectAndRepeatFlowWithLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    },
    block: suspend (T) -> Unit
) {
    collectFlowWithLifeCycle(
        lifecycle = ProcessLifecycleOwner.get().lifecycle,
        minActiveState = minActiveState,
        exceptionHandler = exceptionHandler,
        block = block
    )
}
