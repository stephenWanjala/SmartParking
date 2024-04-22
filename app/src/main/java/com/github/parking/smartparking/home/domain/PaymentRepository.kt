package com.github.parking.smartparking.home.domain

import com.github.parking.smartparking.auth.core.util.Resource
import com.github.parking.smartparking.home.domain.model.STKPushResponse
import com.github.parking.smartparking.home.domain.model.TransactionDetails
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    suspend fun sendSTKPush(request:TransactionDetails): Flow<Resource<STKPushResponse>>
}