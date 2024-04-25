package com.github.parking.smartparking.home.domain


import com.github.parking.smartparking.auth.core.util.Resource
import com.github.parking.smartparking.home.data.repository.Payment
import com.github.parking.smartparking.home.domain.model.ParkingProvider

import com.github.parking.smartparking.home.domain.model.STKPushQuery
import com.github.parking.smartparking.home.domain.model.STKPushQueryResponse
import com.github.parking.smartparking.home.domain.model.STKPushResponse
import com.github.parking.smartparking.home.domain.model.Slot
import com.github.parking.smartparking.home.domain.model.TransactionDetails
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

interface PaymentRepository {
   fun sendSTKPush(request:TransactionDetails):Resource<STKPushResponse>
    fun querySTKPush(request:STKPushQuery):Resource<STKPushQueryResponse>
    suspend fun storePaymentAndUpdates(user: FirebaseUser, payment: Payment, provider: ParkingProvider, slot: Slot): Task<Void>
}