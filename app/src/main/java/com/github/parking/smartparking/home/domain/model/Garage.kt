package com.github.parking.smartparking.home.domain.model

data class Garage(
    val id: String,
    val name: String,
    val floors: Int,
    val slots: List<Slot>
)