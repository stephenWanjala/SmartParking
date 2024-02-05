package com.github.stephenwanjala.smartparking.auth.signin.presentation
sealed class LoginUiEvent{
    data class EnteredEmail(val value: String): LoginUiEvent()
    data class EnteredPassword(val value: String): LoginUiEvent()
    data object TogglePasswordVisibility : LoginUiEvent()

    data object Login : LoginUiEvent()

}
