package com.github.parking.smartparking.home.domain.model

data class Slot(
    val id: String,
    val number: Int,
    val floor: Int,
    val isOccupied: Boolean
)