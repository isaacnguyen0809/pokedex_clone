package com.isaac.pokedex_clone.presentation.detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaac.pokedex_clone.data.model.PokemonInfo
import com.isaac.pokedex_clone.domain.usecase.GetPokemonInfoUseCase
import com.isaac.pokedex_clone.utils.OneTimeEvent
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
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<OneTimeEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun getPokemonInfo(name: String) {
        _uiState.update { DetailUiState.Loading }
        viewModelScope.launch {
            getPokemonInfoUseCase.invoke(name).onSuccess { data ->
                _uiState.update { DetailUiState.Success(pokemonInfo = data) }
            }.onError { _, message, _ ->
                _eventChannel.send(OneTimeEvent.Toast(message = message))
                _uiState.update { DetailUiState.Error(error = message) }
            }.onException { e ->
                _eventChannel.send(OneTimeEvent.Toast(message = e.message))
                _uiState.update { DetailUiState.Error(error = e.message) }
            }
        }
    }
}