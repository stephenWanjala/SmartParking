package com.github.stephenwanjala.smartparking.auth.sigup.presentation

sealed class SignupEvent{
    data class EnteredUsername(val value: String): SignupEvent()
    data class EnteredEmail(val value: String): SignupEvent()
    data class EnteredPassword(val value: String): SignupEvent()
    data object TogglePasswordVisibility : SignupEvent()

    data object Signup : SignupEvent()
}
