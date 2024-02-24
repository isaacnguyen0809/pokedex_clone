package com.isaac.pokedex_clone.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaac.pokedex.clone.UserLocal
import com.isaac.pokedex_clone.common.eventbus.EventBus
import com.isaac.pokedex_clone.common.eventbus.EventType
import com.isaac.pokedex_clone.domain.usecase.AuthUseCase
import com.isaac.pokedex_clone.utils.NavigationEvent
import com.isaac.pokedex_clone.utils.UiEvent
import com.isaac.pokedex_clone.utils.onError
import com.isaac.pokedex_clone.utils.onException
import com.isaac.pokedex_clone.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val eventBus: EventBus,
) : ViewModel() {

    private val _uiMutableStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Initial)

    val uiStateFlow = _uiMutableStateFlow.asStateFlow()

    val userFlow: Flow<UserLocal?> = authUseCase.getUser().distinctUntilChanged()

    private val _demoStateFlow = MutableStateFlow("")

    val demoStateFlow = _demoStateFlow.asStateFlow()

    private val _navigationEventFlow = MutableStateFlow<UiEvent<NavigationEvent>?>(null)
    val navigationEventFlow = _navigationEventFlow.asStateFlow()


    fun login() {
//        viewModelScope.launch {
//            eventBus.post(EventType.EventLoginSuccess(userID = "1"))
//            _uiMutableStateFlow.value = LoginUiState.Loading
//            authUseCase.login()
//                .onSuccess {
//                    authUseCase.saveLoginResponseToUserLocal(it)
//                    _uiMutableStateFlow.value = LoginUiState.Success(it)
//                    _navigationEventFlow.value = object : UiEvent<NavigationEvent> {
//                        override val data: NavigationEvent = NavigationEvent.NavigateToLogin
//                        override val onConsumed: () -> Unit = {
//                            _navigationEventFlow.value = null
//                        }
//                    }
//                    eventBus.post(EventType.EventLoginSuccess(userID = "5"))
//                }.onException {
//                    _uiMutableStateFlow.value = LoginUiState.Error(it)
//                }.onError { _, _, t ->
//                    _uiMutableStateFlow.value = LoginUiState.Error(Exception(t.message))
//                }
//        }
        viewModelScope.launch(Dispatchers.Default) {
            eventBus.produceEvent(EventType.EventLoginSuccess(userID = "1"))
        }
        viewModelScope.launch {
            eventBus.produceEvent(EventType.EventLoginSuccess(userID = "2"))
            eventBus.produceEvent(EventType.EventLoginSuccess(userID = "3"))
            eventBus.produceEvent(EventType.EventLoginSuccess(userID = "4"))
        }

    }

    fun callDemo() {
        viewModelScope.launch {
            authUseCase.callDemo()
                .onSuccess {
                    _demoStateFlow.value = it
                }.onException {
                    _demoStateFlow.value = it.message.orEmpty()
                }.onError { code, message, errorResponse ->
                    _demoStateFlow.value = code.toString() + message + errorResponse.message
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout()
        }
    }
}