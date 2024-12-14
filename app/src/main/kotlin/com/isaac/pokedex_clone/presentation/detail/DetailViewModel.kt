package com.isaac.pokedex_clone.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaac.pokedex_clone.data.mapper.Pokemon
import com.isaac.pokedex_clone.data.model.PokemonInfo
import com.isaac.pokedex_clone.domain.usecase.FavoritePokemonUseCase
import com.isaac.pokedex_clone.domain.usecase.GetPokemonInfoUseCase
import com.isaac.pokedex_clone.presentation.home.viewmodel.DislikedPokemonEvent
import com.isaac.pokedex_clone.presentation.home.viewmodel.LikedPokemonEvent
import com.isaac.pokedex_clone.utils.OneTimeEvent
import com.isaac.pokedex_clone.utils.UiEvent
import com.isaac.pokedex_clone.utils.onError
import com.isaac.pokedex_clone.utils.onException
import com.isaac.pokedex_clone.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface DetailUiState {
    data object Initial : DetailUiState
    data object Loading : DetailUiState
    data class Success(val pokemonInfo: PokemonInfo) : DetailUiState
    data class Error(val error: String?) : DetailUiState
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPokemonInfoUseCase: GetPokemonInfoUseCase,
    private val favoritePokemonUseCase: FavoritePokemonUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<OneTimeEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _likedPokemonStateFlow = MutableStateFlow<UiEvent<LikedPokemonEvent>?>(null)
    val likedPokemonStateFlow = _likedPokemonStateFlow.asStateFlow()

    private val _dislikeStateFlow = MutableStateFlow<UiEvent<DislikedPokemonEvent>?>(null)
    val disLikeStateFlow = _dislikeStateFlow.asStateFlow()

    fun getPokemonInfo(name: String) {
        _uiState.update { DetailUiState.Loading }
        viewModelScope.launch {
            getPokemonInfoUseCase.invoke(name).onSuccess { data ->
                favoritePokemonUseCase.getAllListFavoritePokemon().onSuccess { listFavorite ->
                    var updatedPokemonInfo = data
                    for (item in listFavorite) {
                        if (updatedPokemonInfo.name.lowercase() == item.name.lowercase()) {
                            updatedPokemonInfo = updatedPokemonInfo.copy(isFavored = true)
                            break
                        }
                    }
                    _uiState.update { DetailUiState.Success(pokemonInfo = updatedPokemonInfo) }
                }
            }.onError { _, message, _ ->
                _eventChannel.send(OneTimeEvent.Toast(message = message))
                _uiState.update { DetailUiState.Error(error = message) }
            }.onException { e ->
                _eventChannel.send(OneTimeEvent.Toast(message = e.message))
                _uiState.update { DetailUiState.Error(error = e.message) }
            }
        }
    }

    fun likePokemon(pokemon: Pokemon, onExecute: () -> Unit) {
        viewModelScope.launch {
            _likedPokemonStateFlow.update {
                val isSuccess = favoritePokemonUseCase.likePokemon(pokemon).isSuccess
                if (isSuccess) {
                    favoritePokemonUseCase.getAllListFavoritePokemon().onSuccess { listFavorite ->
                        val currentState = _uiState.value as DetailUiState.Success
                        var updatedPokemonInfo = currentState.pokemonInfo
                        for (item in listFavorite) {
                            if (updatedPokemonInfo.name.lowercase() == item.name.lowercase()) {
                                updatedPokemonInfo = updatedPokemonInfo.copy(isFavored = true)
                                break
                            }
                        }
                        _uiState.value = currentState.copy(pokemonInfo = updatedPokemonInfo)
                    }
                    onExecute.invoke()
                }
                object : UiEvent<LikedPokemonEvent> {
                    override val data: LikedPokemonEvent = LikedPokemonEvent(isSuccess)
                    override val onConsumed: () -> Unit = { _likedPokemonStateFlow.update { null } }
                }
            }
        }
    }

    fun dislikePokemon(pokemon: Pokemon, onExecute: () -> Unit) {
        viewModelScope.launch {
            _dislikeStateFlow.update {
                val isSuccess = favoritePokemonUseCase.unlikePokemon(pokemon).isSuccess
                if (isSuccess) {
                    favoritePokemonUseCase.getAllListFavoritePokemon().onSuccess { listFavorite ->
                        val currentState = _uiState.value as DetailUiState.Success
                        var updatedPokemonInfo = currentState.pokemonInfo
                        updatedPokemonInfo = updatedPokemonInfo.copy(
                            isFavored = checkSeeIfPokemonWasFavored(
                                listFavorite,
                                updatedPokemonInfo
                            )
                        )
                        _uiState.update {
                            currentState.copy(pokemonInfo = updatedPokemonInfo)
                        }
                    }
                    onExecute.invoke()
                }
                object : UiEvent<DislikedPokemonEvent> {
                    override val data: DislikedPokemonEvent = DislikedPokemonEvent(isSuccess)
                    override val onConsumed: () -> Unit = { _dislikeStateFlow.update { null } }
                }

            }
        }
    }

    private fun checkSeeIfPokemonWasFavored(listPokemon: List<Pokemon>, pokemonInfo: PokemonInfo): Boolean {
        val listFavored = listPokemon.filter { it.name.lowercase() == pokemonInfo.name.lowercase() }
        Log.d("DUCKHANH", "checkSeeIfPokemonWasFavored: $listFavored")
        return !listFavored.isEmpty()
    }
}