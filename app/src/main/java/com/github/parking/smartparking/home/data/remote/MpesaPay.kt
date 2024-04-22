package com.github.parking.smartparking.home.data.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.github.parking.smartparking.home.domain.model.AccessToken
import com.github.parking.smartparking.home.domain.model.STKPush
import com.github.parking.smartparking.home.domain.model.TransactionDetails
import com.github.parking.smartparking.home.domain.utils.Constants
import com.github.parking.smartparking.home.domain.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MpesaPay {
    private var mApiClient = ApiClient()
    private var model = TransactionDetails()

    fun sendPush(
        phoneNumber: String,
        cash: String,
        payBill: String,
        partyA: String,
        partyB: String,
        accReference: String,
        transDesc: String,
        transactionType: String,
        passKey: String,
        callbackUrl: String
    ) {
        model.phoneNumber = phoneNumber
        model.payBill = payBill
        model.partyB = partyB
        model.accReference = accReference
        model.description = transDesc
        model.type = transactionType
        model.cashAmount = cash
        model.partyA = partyA
        model.passKey = passKey
        model.callBackUrl = callbackUrl
        getAccessToken(model)
    }

    fun sendPush(transactionDetails: TransactionDetails) {
        getAccessToken(transactionDetails)
    }

    fun sendPush(
        phoneNumber: String, cash: String, payBill: String, partyB: String,
        accReference: String, transDesc: String, passKey: String, callbackUrl: String
    ) {
        sendPush(
            phoneNumber,
            cash,
            payBill,
            partyB,
            partyB,
            accReference,
            transDesc,
            Constants.TRANSACTION_TYPE,
            passKey,
            callbackUrl
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    internal fun performSTKPush(
        transactionDetails: TransactionDetails
    ) {
        val timestamp = Utils.timestamp
        val stkPush = STKPush(
            transactionDetails.payBill!!,
            Utils.getPassword(
                transactionDetails.payBill!!,
                transactionDetails.passKey!!, timestamp
            ),
            timestamp,
            transactionDetails.type!!,
            transactionDetails.cashAmount!!,
            Utils.sanitizePhoneNumber(transactionDetails.phoneNumber!!),
            transactionDetails.partyB!!,
            Utils.sanitizePhoneNumber(transactionDetails.phoneNumber!!),
            transactionDetails.callBackUrl!!,
            transactionDetails.accReference!!, //The account reference
            transactionDetails.description!!  //The transaction description
        )

        mApiClient.setGetAccessToken(false)

        mApiClient.mpesaService().sendPush(stkPush).enqueue(object : Callback<STKPush> {
            override fun onResponse(call: Call<STKPush>, response: Response<STKPush>) {
                try {
                    if (response.isSuccessful) {
                        Log.v("Resp", response.body().toString())
                    } else {

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<STKPush>, t: Throwable) {

            }
        })
    }

    internal fun getAccessToken(transactionDetails: TransactionDetails) {
        mApiClient.setGetAccessToken(true)
        mApiClient.mpesaService().accessToken().enqueue(object : Callback<AccessToken> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {

                if (response.isSuccessful) {
                    mApiClient.setAuthToken(response.body()!!.accessToken)
                    performSTKPush(transactionDetails)
                    Log.v("Resp", response.body().toString())
                } else Log.v("Resp", response.body().toString())
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}


