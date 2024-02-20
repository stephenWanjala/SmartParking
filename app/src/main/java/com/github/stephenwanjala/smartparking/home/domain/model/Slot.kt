package com.github.stephenwanjala.smartparking.home.domain.model

data class Slot(
    val id: String,
    val number: Int,
    val floor: Int,
    val isOccupied: Boolean
)