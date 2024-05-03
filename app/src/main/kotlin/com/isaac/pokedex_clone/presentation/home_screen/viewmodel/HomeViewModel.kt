package com.isaac.pokedex_clone.presentation.home_screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaac.pokedex_clone.data.mapper.Pokemon
import com.isaac.pokedex_clone.data.mapper.toDomain
import com.isaac.pokedex_clone.domain.usecase.FavouritePokemonUseCase
import com.isaac.pokedex_clone.domain.usecase.GetListPokemonUseCase
import com.isaac.pokedex_clone.presentation.favourite_screen.FavouriteUiState
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

data class LikedPokemonEvent(
    val isLikedSuccessful: Boolean,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getListPokemonUseCase: GetListPokemonUseCase,
    private val favouritePokemonUseCase: FavouritePokemonUseCase,
) : ViewModel() {

    private val _uiMutableStateFlow = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiStateFlow = _uiMutableStateFlow.asStateFlow()

    private val _errorEventFlow = MutableStateFlow<UiEvent<OneTimeEvent>?>(null)
    val errorEventFlow = _errorEventFlow.asStateFlow()

    private val _likedPokemonStateFlow = MutableStateFlow<UiEvent<LikedPokemonEvent>?>(null)
    val likedPokemonStateFlow = _likedPokemonStateFlow.asStateFlow()

    private val _dislikeStateFlow = MutableStateFlow<UiEvent<OneTimeEvent>?>(null)
    val disLikeStateFlow = _dislikeStateFlow.asStateFlow()

    private val _favorStateFlow = MutableStateFlow<FavouriteUiState>(FavouriteUiState.Loading)
    internal val favorStateFlow = _favorStateFlow.asStateFlow()

    init {
        Timber.d("Init HomeViewModel")
        getListPokemon()
        getListFavourite()
    }

    fun getListFavourite() {
        viewModelScope.launch {
            favouritePokemonUseCase.getAllListFavouritePokemon().onSuccess { result ->
                _favorStateFlow.update {
                    FavouriteUiState.Success(result)
                }
            }
                .onFailure { e ->
                    _favorStateFlow.update {
                        FavouriteUiState.Error(e)
                    }
                }
        }
    }

    fun likePokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            _likedPokemonStateFlow.update {
                val isSuccess = favouritePokemonUseCase.likePokemon(pokemon).isSuccess
                if (isSuccess) {
                    getListFavourite()
                    favouritePokemonUseCase.getAllListFavouritePokemon().onSuccess { listFavourite ->
                        val currentState = _uiMutableStateFlow.value as HomeUiState.Success
                        val fullList = currentState.data.toMutableList()
                        fullList.forEachIndexed { index, pokemon ->
                            listFavourite.forEach { inner ->
                                if (pokemon?.name?.lowercase() == inner.name.lowercase()) {
                                    fullList[index] = fullList[index]?.copy(isFavourite = true)
                                }
                            }
                        }
                        _uiMutableStateFlow.value = currentState.copy(data = fullList)
                    }
                }
                object : UiEvent<LikedPokemonEvent> {
                    override val data: LikedPokemonEvent = LikedPokemonEvent(isSuccess)
                    override val onConsumed: () -> Unit = { _likedPokemonStateFlow.update { null } }
                }
            }
        }
    }

    fun dislikePokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            _dislikeStateFlow.update {
                val isSuccess = favouritePokemonUseCase.unlikePokemon(pokemon).isSuccess
                if (isSuccess) {
                    favouritePokemonUseCase.getAllListFavouritePokemon().onSuccess { listFavourite ->
                        val currentState = _uiMutableStateFlow.value as HomeUiState.Success
                        val fullList =
                            currentState.data.map { it?.copy(isFavourite = false) }.toMutableList()
                        fullList.forEachIndexed { index, pokemon ->
                            listFavourite.forEach { inner ->
                                if (pokemon?.name?.lowercase() == inner.name.lowercase()) {
                                    fullList[index] = fullList[index]?.copy(isFavourite = true)
                                }
                            }
                        }
                        _uiMutableStateFlow.value = currentState.copy(data = fullList)
                    }
                    getListFavourite()
                }
                val message = if (isSuccess) "Delete favourite successful" else "Delete favourite failed"
                object : UiEvent<OneTimeEvent> {
                    override val data: OneTimeEvent = OneTimeEvent.Toast(null, message, null)
                    override val onConsumed: () -> Unit = { _dislikeStateFlow.update { null } }
                }

            }
        }
    }

    fun getListPokemon(isRefresh: Boolean = false) {
        _uiMutableStateFlow.value = if (isRefresh) HomeUiState.RefreshList else HomeUiState.Loading
        viewModelScope.launch {
            getListPokemonUseCase.invoke(page = 0).onSuccess { data ->
                favouritePokemonUseCase.getAllListFavouritePokemon().onSuccess { listFavourite ->
                    val fullList = data.result.map { it.toDomain() }.toMutableList()
                    fullList.forEachIndexed { index, pokemon ->
                        listFavourite.forEach { inner ->
                            if (pokemon.name.lowercase() == inner.name.lowercase()) {
                                fullList[index] = fullList[index].copy(isFavourite = true)
                            }
                        }
                    }
                    _uiMutableStateFlow.value = HomeUiState.Success(
                        data = fullList,
                        currentPage = 1,
                        isLoadingNextPage = false,
                    )
                }

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
            return
        }
        if (!currentUiState.isLoadingNextPage) {
            _uiMutableStateFlow.update {
                currentUiState.copy(
                    isLoadingNextPage = true,
                )
            }
            execute.invoke()
            val nextPage = currentUiState.currentPage + 1

            viewModelScope.launch {
                getListPokemonUseCase.invoke(nextPage).onSuccess { data ->
                    favouritePokemonUseCase.getAllListFavouritePokemon().onSuccess { listFavourite ->
                        val fullList = (currentUiState.data + data.result.map { it.toDomain() }).toMutableList()
                        fullList.forEachIndexed { index, pokemon ->
                            listFavourite.forEach { inner ->
                                if (pokemon?.name?.lowercase() == inner.name.lowercase()) {
                                    fullList[index] = fullList[index]?.copy(isFavourite = true)
                                }
                            }
                        }
                        _uiMutableStateFlow.update {
                            HomeUiState.Success(
                                data = fullList,
                                currentPage = nextPage,
                                isLoadingNextPage = false,
                            )
                        }
                    }
                }.onException {
                    _uiMutableStateFlow.update {
                        HomeUiState.Success(
                            data = (currentUiState.data),
                            currentPage = currentUiState.currentPage,
                            isLoadingNextPage = false,
                        )
                    }
                }.onError { _, _, _ ->
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