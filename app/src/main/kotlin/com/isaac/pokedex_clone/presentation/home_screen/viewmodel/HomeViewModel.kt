package com.isaac.pokedex_clone.presentation.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaac.pokedex_clone.domain.usecase.GetListPokemonUseCase
import com.isaac.pokedex_clone.utils.getStatusCode
import com.isaac.pokedex_clone.utils.onError
import com.isaac.pokedex_clone.utils.onException
import com.isaac.pokedex_clone.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getListPokemonUseCase: GetListPokemonUseCase,
) : ViewModel() {

    private val _uiMutableStateFlow = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val uiStateFlow = _uiMutableStateFlow.asStateFlow()

    init {
        Timber.d("Init HomeViewModel")
        getListPokemon()
    }

    private fun getListPokemon() {
        viewModelScope.launch {
            getListPokemonUseCase.invoke(LIMIT, OFF_SET)
                .onSuccess {
                    _uiMutableStateFlow.value = HomeUiState.Success(it.result)
                }
                .onError { code, _ ->
                    Timber.e("Code: $code")
                    _uiMutableStateFlow.value = HomeUiState.Error(Exception(code.getStatusCode().toString()))
                }
                .onException {
                    _uiMutableStateFlow.value = HomeUiState.Error(it)
                }
        }

    }

    companion object {
        private const val LIMIT = 20
        private const val OFF_SET = 0
    }
}