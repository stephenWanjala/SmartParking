package com.github.parking.smartparking.home.domain.model

data class STKResponse(
    val CheckoutRequestID: String,
    val CustomerMessage: String,
    val MerchantRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String
)