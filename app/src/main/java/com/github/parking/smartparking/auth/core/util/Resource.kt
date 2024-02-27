package com.github.parking.smartparking.auth.core.util

sealed class Resource<T>(val data: T? = null, val uiText: UiText? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(uiText: UiText, data: T? = null): Resource<T>(data, uiText)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}