package com.github.parking.smartparking.auth.signin.presentation

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.parking.smartparking.auth.core.domain.repository.AuthRepository
import com.github.parking.smartparking.auth.core.util.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state
    private val _loginUIState = MutableSharedFlow<LoginState>()
    val loginUpIState = _loginUIState.asSharedFlow()

    val isCurrentUserExist: Flow<Boolean>


    init{
        isCurrentUserExist= authRepository.isCurrentUserExist()

    }
    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EnteredEmail -> {
                _state.update { it.copy(email = event.value)}
            }
            is LoginUiEvent.EnteredPassword -> {
                _state.update { it.copy(password = event.value)}
            }
            is LoginUiEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !state.value.isPasswordVisible)}
            }

            is LoginUiEvent.Login -> {
                loginUser(email = _state.value.email, password = _state.value.password)
            }

        }
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signInWithEmailAndPassword(email, password).onEach { result ->

                when (result) {
                    is Resource.Success -> {
                        _loginUIState.emit(
                            LoginState(login = result.data)
                        )
                        _state.update {
                            it.copy(
                                email = "", password = ""
                            )

                        }
                    }
                    is Resource.Error -> {
                        _loginUIState.emit(
                            LoginState(error = result.uiText)
                        )
                        _state.update {
                            it.copy(
                                email = "", password = ""
                            )

                        }
                    }
                    is Resource.Loading -> {
                        _loginUIState.emit(
                            LoginState(isLoading = true)
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }



    fun getUserEmail(): String {
        var email: String by mutableStateOf("")
        viewModelScope.launch {
            authRepository.getCurrentUserEmail().collect {
                email = it
            }
        }
        return email
    }



fun signOut() {
    viewModelScope.launch(Dispatchers.IO) {
        authRepository.signOut()
    }
}
}