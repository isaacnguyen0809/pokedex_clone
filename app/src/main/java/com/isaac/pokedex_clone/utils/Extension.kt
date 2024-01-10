package com.isaac.pokedex_clone.utils

import android.content.res.Resources.getSystem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException

val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()

//Flow
inline fun <T> Flow<T>.collectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (value: T) -> Unit,
): Job = owner.lifecycleScope.launch {
    owner.lifecycle.repeatOnLifecycle(state = minActiveState) {
        Timber.d("Start collecting...")
        collect { action(it) }
    }
}

//Coroutines - https://github.com/Kotlin/kotlinx.coroutines/issues/1814
@OptIn(ExperimentalContracts::class)
@Suppress("RedundantSuspendModifier")
suspend inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}