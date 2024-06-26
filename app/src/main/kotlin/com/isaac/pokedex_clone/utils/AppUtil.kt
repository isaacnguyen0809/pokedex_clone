package com.isaac.pokedex_clone.utils

import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.isaac.pokedex_clone.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException

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

inline fun <T> Flow<T>.collectEventChannel(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (value: T) -> Unit,
): Job = owner.lifecycleScope.launch {
    owner.lifecycle.repeatOnLifecycle(state = minActiveState) {
        withContext(Dispatchers.Main.immediate) {
            collect { action(it) }
        }
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

fun View.triggerAnim(isShow: Boolean) {
    val openEnter = AnimationUtils.loadAnimation(
        this.context, R.anim.fragment_open_enter
    ).also {
        it.duration = 500
    }
    val openExit = AnimationUtils.loadAnimation(
        this.context, R.anim.fragment_open_exit
    )
    if (isShow) {
        this.startAnimation(openEnter)
        this.isVisible = true
    } else {
        this.startAnimation(openExit)
        this.isVisible = false
    }

}

object PokemonTypeUtils {

    fun getTypeColor(type: String): Int {
        return when (type) {
            "fighting" -> R.color.fighting
            "flying" -> R.color.flying
            "poison" -> R.color.poison
            "ground" -> R.color.ground
            "rock" -> R.color.rock
            "bug" -> R.color.bug
            "ghost" -> R.color.ghost
            "steel" -> R.color.steel
            "fire" -> R.color.fire
            "water" -> R.color.water
            "grass" -> R.color.grass
            "electric" -> R.color.electric
            "psychic" -> R.color.psychic
            "ice" -> R.color.ice
            "dragon" -> R.color.dragon
            "fairy" -> R.color.fairy
            "dark" -> R.color.dark
            else -> R.color.gray_21
        }
    }
}
