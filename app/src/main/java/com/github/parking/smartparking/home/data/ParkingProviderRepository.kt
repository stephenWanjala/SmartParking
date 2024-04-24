package com.github.parking.smartparking.home.data

import android.util.Log
import com.github.parking.smartparking.home.domain.model.ParkingProvider
import com.github.parking.smartparking.home.domain.model.Slot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object ParkingProviderRepository {

    private const val TAG = "ParkingProviderRepository"

    fun fetchParkingProvidersWithSlots(
        onSuccess: (List<ParkingProvider>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val parkingProvidersCollection = db.collection("parkingProviders")

        // Query all documents from the "parkingProviders" collection
        parkingProvidersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val parkingProviders = mutableListOf<ParkingProvider>()

                // Loop through each ParkingProvider document
                for (document in querySnapshot) {
                    // Parse ParkingProvider data from Firestore document
                    val id = document.getString("id") ?: ""
                    val name = document.getString("name") ?: ""
                    val hourlyRate = document.getDouble("hourlyRate") ?: 0.0
                    val location = document.getString("location") ?: ""
                    val imageUrl = document.getLong("imageUrl")?.toInt() ?: 0
                    val availableSlots = document.getLong("availableSlots")?.toInt() ?: 0
                    val isFull = document.getBoolean("isFull") ?: false

                    // Fetch Slots for the current ParkingProvider from subcollection
                    val slots = mutableListOf<Slot>()
                    val slotsCollection = document.reference.collection("slots")

                    slotsCollection.get()
                        .addOnSuccessListener { slotsQuerySnapshot ->
                            // Loop through each Slot document
                            for (slotDocument in slotsQuerySnapshot) {
                                // Parse Slot data from Firestore document
                                val slotId = slotDocument.getString("id") ?: ""
                                val number = slotDocument.getLong("number")?.toInt() ?: 0
                                val floor = slotDocument.getLong("floor")?.toInt() ?: 0
                                val isOccupied = slotDocument.getBoolean("isOccupied") ?: false

                                // Create Slot object
                                val slot = Slot(
                                    id = slotId,
                                    number = number,
                                    floor = floor,
                                    isOccupied = isOccupied
                                )

                                // Add Slot to the list
                                slots.add(slot)
                            }

                            // Create ParkingProvider object with fetched Slots
                            val provider = ParkingProvider(
                                id = id,
                                name = name,
                                hourlyRate = hourlyRate,
                                location = location,
                                imageUrl = imageUrl,
                                slots = slots,
//                                availableSlots = availableSlots,
//                                isFull = isFull
                            )

                            // Add ParkingProvider to the list
                            parkingProviders.add(provider)

                            // Check if all ParkingProviders have been processed
                            if (parkingProviders.size == querySnapshot.size()) {
                                // Invoke onSuccess callback with list of ParkingProviders
                                onSuccess(parkingProviders)
                            }
                        }
                        .addOnFailureListener { e ->
                            // Handle failure to fetch Slots
                            onFailure(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure to fetch ParkingProviders
                onFailure(e)
            }
    }
}
