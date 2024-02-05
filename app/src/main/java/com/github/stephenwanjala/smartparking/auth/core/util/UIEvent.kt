package com.github.stephenwanjala.smartparking.auth.core.util

sealed class UiEvent : Event() {
    data class ShowSnackbar(val uiText: UiText) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data object NavigateUp : UiEvent()
    data object OnLogin : UiEvent()
    data class ShowToast(val uiText: UiText) : UiEvent()

}