package com.github.stephenwanjala.smartparking.auth.sigup.presentation

import com.github.stephenwanjala.smartparking.auth.core.util.UiText
import com.google.firebase.auth.AuthResult


data class SignUpUIState(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isUserNameError: UsernameError? = null,
    val isEmailError: EmailError? = null,
    val isPasswordError: PasswordError? = null,
    val isSignUpButtonEnabled: Boolean = false,

    ) {
    sealed class UsernameError {
        data object FieldEmpty : UsernameError()
        data object InputTooShort : UsernameError()
    }
    sealed class EmailError {
        data object FieldEmpty : EmailError()
        data object InvalidEmail: EmailError()
    }
    sealed class PasswordError {
        data object FieldEmpty: PasswordError()
        data object InvalidPassword : PasswordError()
        data object InputTooShort : PasswordError()
    }


}

data class SignUpState(
    val isLoading: Boolean = false,
    val signUp: AuthResult?= null,
    val error: UiText? = null
)
