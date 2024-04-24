package com.github.parking.smartparking.home.domain.model

import android.os.Parcelable
import com.github.parking.smartparking.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParkingProvider(
    val id: String,
    val name: String,
    val slots: List<Slot>,
    val hourlyRate: Double,
    val location: String,
    val imageUrl: Int
) : Parcelable {
    val availableSlots: Int
        get() = slots.count { !it.isOccupied }

    @IgnoredOnParcel
    val totalSlots: Int = slots.size
    val isFull: Boolean
        get() = availableSlots == 0


     companion object{
        val providers = listOf(
            ParkingProvider(
                id = "1",
                name = "Secure Garage Parking",
                slots = listOf(
                    Slot("1", 1, 1, true),
                    Slot("2", 2, 1, true),
                    Slot("3", 3, 1, false),
                    Slot("4", 4, 1, true),
                    Slot("5", 5, 1, true),
                    Slot("6", 6, 1, true),
                    Slot("7", 7, 1, false),
                    Slot("8", 8, 1, false),
                    Slot("9", 9, 1, false),

                ),
                hourlyRate = 1.0,
                location = "123 Main St, San Francisco, CA",
                imageUrl = R.drawable.parking_1
            ),
            ParkingProvider(
                id = "2",
                name = "Water Loon Parking",
                slots = listOf(
                    Slot("1", 1, 1, false),
                    Slot("2", 2, 1, true),
                    Slot("3", 3, 1, false),
                    Slot("4", 4, 1, true),
                    Slot("5", 5, 1, false),
                    Slot("6", 6, 1, true),
                    Slot("7", 7, 1, true),
                    Slot("8", 8, 1, true),
                    Slot("9", 9, 1, true),
                    Slot("10", 10, 1, true),
                    Slot("11", 11, 1, false),
                    Slot("12", 12, 1, true),
                ),
                hourlyRate = 10.0,
                location = "Nairobi CBD, Nairobi, Kenya",
                imageUrl = R.drawable.parking_2,
            ),
        )

     }
}

