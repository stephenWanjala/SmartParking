package com.github.stephenwanjala.smartparking.auth.sigup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.stephenwanjala.smartparking.auth.core.util.Resource
import com.github.stephenwanjala.smartparking.auth.sigup.presentation.SignUpState
import com.github.stephenwanjala.smartparking.auth.sigup.presentation.SignUpUIState
import com.github.stephenwanjala.smartparking.auth.sigup.presentation.SignupEvent
import com.github.stephenwanjala.smartparking.auth.sigup.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpUIState())
    val state: StateFlow<SignUpUIState> = _state

    private val _signUpUIState = MutableSharedFlow<SignUpState>()
    val signUpIState = _signUpUIState.asSharedFlow()

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.EnteredUsername -> {
                _state.update { it.copy(userName = event.value) }
            }

            is SignupEvent.EnteredEmail -> {
                _state.update { it.copy(email = event.value) }
            }

            is SignupEvent.EnteredPassword -> {
                _state.update { it.copy(password = event.value) }
            }

            is SignupEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !state.value.isPasswordVisible) }
            }

            is SignupEvent.Signup -> {
                signUp(
                    email = _state.value.email,
                    password = _state.value.password,
                    userName = _state.value.userName
                )
            }


        }


    }

    private fun signUp(userName: String, email: String, password: String) =
        viewModelScope.launch {
            signUpUseCase(userName, email, password).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _signUpUIState.emit(
                            SignUpState(
                                signUp = result.data
                            )


                        )
                    }
                    is Resource.Error -> {
                        _signUpUIState.emit(SignUpState(error = result.uiText))
                    }
                    is Resource.Loading -> {
                        _signUpUIState.emit(SignUpState(isLoading = true))
                    }
                }
            }.launchIn(viewModelScope)
        }

}