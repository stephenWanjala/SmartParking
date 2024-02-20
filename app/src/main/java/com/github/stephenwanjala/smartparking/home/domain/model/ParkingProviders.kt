package com.github.stephenwanjala.smartparking.home.domain.model

data class ParkingProvider(
    val id: String,
    val name: String,
    val locations: List<ParkingLocation>,
    val hourlyRate: Double
)

