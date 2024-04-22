package com.github.parking.smartparking.home.domain.model

import java.io.Serializable

data class TransactionDetails(
    var phoneNumber: String? = null,
    var passKey: String? = null,
    var cashAmount: Int? = null,
    var payBill: String? = null,
    var accReference: String? = null,
    var callBackUrl: String? = null,
    var partyA: String? = null,
    var partyB: String? = null,
    var description: String? = null,
    var type: String? = null
) : Serializable