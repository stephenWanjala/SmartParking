package com.github.parking.smartparking.home.domain.model

import android.os.Parcelable
import com.github.parking.smartparking.home.data.remote.MpesaPay.toSTkQuery
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class STKPushResponse(
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String,
    val CustomerMessage: String
) : Serializable, Parcelable{
    fun STKPushResponse.toSTkQuery():STKPushQuery=STKPushQuery(
        BusinessShortCode = MerchantRequestID,
        Password = CheckoutRequestID,
        Timestamp = ResponseCode,
        CheckoutRequestID = ResponseDescription
    )
}



