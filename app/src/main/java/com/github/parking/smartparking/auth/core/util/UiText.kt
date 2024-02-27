package com.github.parking.smartparking.auth.core.util

import androidx.annotation.StringRes
import com.github.parking.smartparking.R

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    data class StringResource(@StringRes val id: Int): UiText()

    companion object {
        fun unknownError(): UiText {
            return StringResource(R.string.error_unknown)
        }
    }
}