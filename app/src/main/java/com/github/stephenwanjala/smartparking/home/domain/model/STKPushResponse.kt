package com.github.stephenwanjala.smartparking.home.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class STKPushResponse(
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String,
    val CustomerMessage: String
) : Serializable, Parcelable