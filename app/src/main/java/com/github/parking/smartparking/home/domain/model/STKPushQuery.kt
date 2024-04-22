package com.github.parking.smartparking.home.domain.model

/*
{
   "BusinessShortCode":"174379",
   "Password": "MTc0Mzc5YmZiMjc5TliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTYwMjE2MTY1NjI3",
   "Timestamp":"20160216165627",
   "CheckoutRequestID": "ws_CO_260520211133524545",
}
 */


data class STKPushQuery(
    val BusinessShortCode: String,
    val Password: String,
    val Timestamp: String,
    val CheckoutRequestID: String
)
