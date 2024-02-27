package com.github.parking.smartparking.auth.signin.presentation

sealed class LoginEvent{
    data class EnteredEmail(val value: String): LoginEvent()
    data class EnteredPassword(val value: String): LoginEvent()
    data object TogglePasswordVisibility : LoginEvent()
    data object ToSignUpScreen : LoginEvent()
    data object Login : LoginEvent()
    data object  ToForgotPassword: LoginEvent()
}
