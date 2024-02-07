package com.isaac.pokedex_clone.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaac.pokedex.clone.UserLocal
import com.isaac.pokedex_clone.domain.usecase.AuthUseCase
import com.isaac.pokedex_clone.utils.onException
import com.isaac.pokedex_clone.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private val _uiMutableStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Initial)

    val uiStateFlow = _uiMutableStateFlow.asStateFlow()

    val userFlow: Flow<UserLocal?> = authUseCase.getUser().distinctUntilChanged()

    fun login() {
        viewModelScope.launch {
            _uiMutableStateFlow.value = LoginUiState.Loading
            authUseCase.login().onSuccess {
                authUseCase.saveLoginResponseToUserLocal(it)
                _uiMutableStateFlow.value = LoginUiState.Success(it)
            }.onException {
                _uiMutableStateFlow.value = LoginUiState.Error(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout()
        }
    }
}