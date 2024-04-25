package com.github.parking.smartparking.home.domain.model

import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Slot(
    val id: String,
    val number: Int,
    val floor: Int,
    var isOccupied: Boolean,
    val occupiedBy: Occupier? = null
) : Parcelable,Serializable


@Parcelize
data class Occupier(
    val hours: Int,
    val amount: Int,
    val user: String
) : Parcelable,Serializable
