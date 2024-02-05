package com.github.stephenwanjala.smartparking.auth.core.util

data class PasswordTextFieldState(
    val text: String = "",
    val error: Error? = null,
    val isPasswordVisible: Boolean = false
)