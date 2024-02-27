package com.github.parking.smartparking.auth.sigup.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpModel(
    val email: String,
    val password: String,
    val confirmPassword: String
) : Parcelable
