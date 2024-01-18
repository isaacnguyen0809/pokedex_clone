package com.isaac.pokedex_clone.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaac.pokedex_clone.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiMutableStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Initial)

    val uiStateFlow = _uiMutableStateFlow.asStateFlow()

    fun login() {
        viewModelScope.launch {
//            checkAuthUseCase.invoke().fold(
//                onSuccess = {},
//                onFailure = {
//                    Timber.d("${it}")
//                }
//            )
            _uiMutableStateFlow.value = LoginUiState.Loading
            _uiMutableStateFlow.value = authUseCase.login().fold(
                onSuccess = {
                    LoginUiState.Success(it)
                },
                onFailure = LoginUiState::Error
            )
        }
    }
}