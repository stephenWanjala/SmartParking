package com.github.parking.smartparking.home.data

import android.content.Context
import android.content.SharedPreferences
import com.github.parking.smartparking.home.domain.model.ParkingProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch

object ParkingProviderDataInitializer {

    private const val PREF_NAME = "ParkingProviderDataInitializer"
    private const val PREF_KEY_INSERTED = "data_inserted"

    fun initializeParkingProviders(context: Context) {
        // Get SharedPreferences
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Check if data has already been inserted
        if (!sharedPreferences.getBoolean(PREF_KEY_INSERTED, false)) {
            // Data has not been inserted yet, perform batch insertion
            insertParkingProviders(context)
                .addOnSuccessListener {
                    // Mark data as inserted in SharedPreferences on success
                    sharedPreferences.edit().putBoolean(PREF_KEY_INSERTED, true).apply()
                }
                .addOnFailureListener { e ->
                    // Handle failure to insert data
                    // You may want to log the error or notify the user
                }
        }
    }

    private fun insertParkingProviders(context: Context): Task<Void> {
        val db = FirebaseFirestore.getInstance()
        val parkingProvidersCollection = db.collection("parkingProviders")

        // Batch write operation
        val batch: WriteBatch = db.batch()

        // Loop through each ParkingProvider and add to batch
        for (provider in ParkingProvider.providers) {
            // Create document reference for ParkingProvider
            val providerRef = parkingProvidersCollection.document(provider.id)

            // Map for ParkingProvider data
            val providerData = hashMapOf(
                "id" to provider.id,
                "name" to provider.name,
                "hourlyRate" to provider.hourlyRate,
                "location" to provider.location,
                "imageUrl" to provider.imageUrl,
                "availableSlots" to provider.availableSlots,
                "isFull" to provider.isFull
            )

            // Set ParkingProvider data in batch
            batch.set(providerRef, providerData)

            // Create subcollection reference for Slots under the ParkingProvider
            val slotsCollectionRef = providerRef.collection("slots")

            // Loop through Slots of the current ParkingProvider
            for (slot in provider.slots) {
                // Create document reference for Slot
                val slotRef = slotsCollectionRef.document(slot.id)

                // Map for Slot data
                val slotData = hashMapOf(
                    "id" to slot.id,
                    "number" to slot.number,
                    "floor" to slot.floor,
                    "isOccupied" to slot.isOccupied
                )

                // Set Slot data in batch under the Slots subcollection
                batch.set(slotRef, slotData)
            }
        }

        // Commit the batch write operation
        return batch.commit()
    }
}
