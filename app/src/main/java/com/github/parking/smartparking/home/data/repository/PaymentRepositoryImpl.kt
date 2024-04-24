package com.github.parking.smartparking.home.data.repository

import com.github.parking.smartparking.auth.core.util.Resource
import com.github.parking.smartparking.auth.core.util.UiText
import com.github.parking.smartparking.home.data.remote.MpesaPay
import com.github.parking.smartparking.home.domain.model.STKPushResponse
import com.github.parking.smartparking.home.domain.PaymentRepository
import com.github.parking.smartparking.home.domain.model.STKPushQuery
import com.github.parking.smartparking.home.domain.model.STKPushQueryResponse
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
                emit(Resource.Loading())
                println("Request: $request")
                val response = api.sendPush(request)
                println("Response: $response")
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

    override suspend fun querySTKPush(request: STKPushQuery): Flow<Resource<STKPushQueryResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Loading())
                println("Request: $request")
                val response = api.queryPush(request)
                println("Response: $response")
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