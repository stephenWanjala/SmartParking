package com.github.parking.smartparking.home.domain.model

/*
{
   "ResponseCode":"0",
   "ResponseDescription": "The service request has been accepted successfully",
   "MerchantRequestID":"22205-34066-1",
   "CheckoutRequestID": "ws_CO_13012021093521236557",
   "ResultCode":"0",
   "ResultDesc":"The service request is processed successfully.",
}
 */
data class STKPushQueryResponse(
    val ResponseCode: String,
    val ResponseDescription: String,
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResultCode: String,
    val ResultDesc: String
)
