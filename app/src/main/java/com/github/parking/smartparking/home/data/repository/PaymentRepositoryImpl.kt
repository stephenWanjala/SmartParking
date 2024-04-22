package com.github.parking.smartparking.home.data.repository

import com.github.parking.smartparking.auth.core.util.Resource
import com.github.parking.smartparking.auth.core.util.UiText
import com.github.parking.smartparking.home.data.remote.MpesaPay
import com.github.parking.smartparking.home.domain.model.STKPushResponse
import com.github.parking.smartparking.home.domain.PaymentRepository
import com.github.parking.smartparking.home.domain.model.TransactionDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor() : PaymentRepository {
    private val api = MpesaPay
    override suspend fun sendSTKPush(request: TransactionDetails): Flow<Resource<STKPushResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.sendPush(request)
                emit(Resource.Success(response!!))
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        UiText.DynamicString(
                            e.localizedMessage ?: "An error occurred"
                        )
                    )
                )
            }
        }
    }
}