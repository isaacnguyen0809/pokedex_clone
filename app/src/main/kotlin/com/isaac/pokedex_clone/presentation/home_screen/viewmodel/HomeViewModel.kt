package com.isaac.pokedex_clone.presentation.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaac.pokedex_clone.domain.usecase.GetListPokemonUseCase
import com.isaac.pokedex_clone.utils.OneTimeEvent
import com.isaac.pokedex_clone.utils.UiEvent
import com.isaac.pokedex_clone.utils.onError
import com.isaac.pokedex_clone.utils.onException
import com.isaac.pokedex_clone.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getListPokemonUseCase: GetListPokemonUseCase,
) : ViewModel() {

    private val _uiMutableStateFlow = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val uiStateFlow = _uiMutableStateFlow.asStateFlow()

    private val _errorEventFlow = MutableStateFlow<UiEvent<OneTimeEvent>?>(null)

    val errorEventFlow = _errorEventFlow.asStateFlow()

    init {
        Timber.d("Init HomeViewModel")
        getListPokemon()
    }

    fun getListPokemon(isRefresh: Boolean = false) {
        _uiMutableStateFlow.value = if (isRefresh) HomeUiState.RefreshList else HomeUiState.Loading
        viewModelScope.launch {
            getListPokemonUseCase.invoke(page = 0).onSuccess {
                _uiMutableStateFlow.value = HomeUiState.Success(
                    data = it.result,
                    currentPage = 1,
                    isLoadingNextPage = false,
                )
            }.onError { code, message, errorBody ->
                _uiMutableStateFlow.value = HomeUiState.Error(Exception())
                _errorEventFlow.value = object : UiEvent<OneTimeEvent> {
                    override val data: OneTimeEvent = OneTimeEvent.Toast(code, message, errorBody)
                    override val onConsumed: () -> Unit = { _errorEventFlow.update { null } }
                }
            }.onException {
                _uiMutableStateFlow.value = HomeUiState.Error(Exception())
                _errorEventFlow.value = object : UiEvent<OneTimeEvent> {
                    override val data: OneTimeEvent = OneTimeEvent.Toast(message = it.message)
                    override val onConsumed: () -> Unit = { _errorEventFlow.update { null } }
                }
            }
        }
    }


    internal fun loadNextPage(execute: () -> Unit) {
        val currentUiState = _uiMutableStateFlow.value
        if (currentUiState !is HomeUiState.Success) {
            // ignore, not ready to load next page
            return
        }
        // call 1 time only
        if (!currentUiState.isLoadingNextPage) {
            // toggle loading next page
            _uiMutableStateFlow.update {
                currentUiState.copy(
                    isLoadingNextPage = true,
                )
            }
            execute.invoke()
            val nextPage = currentUiState.currentPage + 1

            viewModelScope.launch {
                getListPokemonUseCase.invoke(nextPage).onSuccess { data ->
                    _uiMutableStateFlow.update {
                        HomeUiState.Success(
                            data = (currentUiState.data + data.result),
                            currentPage = nextPage,
                            isLoadingNextPage = false,
                        )
                    }
                }.onException {
                    _uiMutableStateFlow.update {
                        HomeUiState.Success(
                            data = (currentUiState.data),
                            currentPage = currentUiState.currentPage,
                            isLoadingNextPage = false,
                        )
                    }
                }.onError { code, message, errorResponse ->
                    _uiMutableStateFlow.update {
                        HomeUiState.Success(
                            data = (currentUiState.data),
                            currentPage = currentUiState.currentPage,
                            isLoadingNextPage = false,
                        )
                    }
                }
            }
        }
    }

}