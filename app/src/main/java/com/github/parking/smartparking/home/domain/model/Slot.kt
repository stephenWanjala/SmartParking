package com.github.parking.smartparking.home.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Slot(
    val id: String,
    val number: Int,
    val floor: Int,
    val isOccupied: Boolean
) : Parcelable