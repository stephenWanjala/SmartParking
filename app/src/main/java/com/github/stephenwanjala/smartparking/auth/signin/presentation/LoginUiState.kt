package com.github.stephenwanjala.smartparking.auth.signin.presentation
import com.github.stephenwanjala.smartparking.auth.core.util.UiText
import com.google.firebase.auth.AuthResult


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isEmailError: EmailError? = null,
    val isPasswordError: PasswordError? = null,
    val isLoginButtonEnabled: Boolean = false,
) {
    sealed class EmailError {
        data object FieldEmpty : EmailError()
        data object InvalidEmail : EmailError()
    }

    sealed class PasswordError {
        data object FieldEmpty : PasswordError()
        data object InvalidPassword : PasswordError()
        data object InputTooShort : PasswordError()
    }
}

data class LoginState(
    val isLoading: Boolean = false,
    val login: AuthResult? = null,
    val error: UiText? = null
)

