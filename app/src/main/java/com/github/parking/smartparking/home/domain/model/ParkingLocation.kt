package com.github.parking.smartparking.home.domain.model

data class ParkingLocation(
    val id: String,
    val address: String,
    val garages: List<Garage>
)