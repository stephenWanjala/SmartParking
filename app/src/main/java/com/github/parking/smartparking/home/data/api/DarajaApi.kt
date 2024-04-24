package com.github.parking.smartparking.home.data.api

import com.github.parking.smartparking.home.domain.model.AccessToken
import com.github.parking.smartparking.home.domain.model.STKPush
import com.github.parking.smartparking.home.domain.model.STKPushResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DarajaApi {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun accessToken(): Response<AccessToken>

    @POST("mpesa/stkpush/v1/processrequest")
    fun sendPush(
        @Body stkPush: STKPush
    ):Response<STKPushResponse>

//    callback endpoint

}