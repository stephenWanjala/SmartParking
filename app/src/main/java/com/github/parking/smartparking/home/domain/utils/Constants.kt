package com.github.parking.smartparking.home.domain.utils

object Constants {
    const val CONNECT_TIMEOUT = 60 * 1000
    /**
     * Connection Read timeout duration
     */
    const val READ_TIMEOUT = 60 * 1000
    /**
     * Connection write timeout duration
     */
    const val WRITE_TIMEOUT = 60 * 1000
    /**
     * Base URL
     */
    const val BASE_URL = "https://sandbox.safaricom.co.ke/"


    //STKPush Properties
    const val BUSINESS_SHORT_CODE = "174379"
    const val PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
    const val TRANSACTION_TYPE = "CustomerPayBillOnline"
    const val PARTYB = "174379"
    const val PARTYA = "174379"
    const val DESCRIPTION = "Smart Parking"
    const val ACCOUNT_REFERENCE = "Smart Parking"
    const val CALLBACKURL =
        "https://en8bdjru6met.x.pipedream.net/"
}