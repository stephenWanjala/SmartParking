package com.github.parking.smartparking.home.data.remote

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.Serializable
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Parcelize
data class STKPushResponse(
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String,
    val CustomerMessage: String
) : Serializable, Parcelable

@OptIn(ExperimentalEncodingApi::class)
object MpesaService {
    private val client = OkHttpClient()

    @RequiresApi(Build.VERSION_CODES.O)
    fun initiateSTKPush(
        consumerKey: String = "9gPqB0tjMsuD5AcAMiXyglaIOCYuaCR5L6KMH8Oj9liW2GAY",
        consumerSecret: String = "EaUQ2k7Kc5c9DNzuwqp4FSMK5V9p4yqBFLG5STVi26NCnXwtQr2CeafNMMQDpL7o",
        shortCode: String = "247247",
        passKey: String = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
        phoneNumber: String = "0723441923",
        callbackUrl: String = "https://en8bdjru6met.x.pipedream.net/",
        accountNumber: String = "0723441923",
        amount: Int = 1
    ): STKPushResponse {
        val accessToken = getAccessToken(consumerKey, consumerSecret)
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val password =
            Base64.encode("$shortCode$passKey$timestamp".toByteArray())
        val stkPushJson = JSONObject()
        stkPushJson.put("BusinessShortCode", shortCode)
        stkPushJson.put("Password", password)
        stkPushJson.put("Timestamp", timestamp)
        stkPushJson.put("TransactionType", "CustomerPayBillOnline")
        stkPushJson.put("Amount", amount) // Replace with the amount to charge
        stkPushJson.put("PartyA", phoneNumber)
        stkPushJson.put("PartyB", shortCode)
        stkPushJson.put("PhoneNumber", phoneNumber)
        stkPushJson.put("CallBackURL", callbackUrl)
        stkPushJson.put("AccountNumber", accountNumber)
        stkPushJson.put("AccountReference", "Smart Parking")
        stkPushJson.put("TransactionDesc", "Smart Parking Payment")

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body: RequestBody = stkPushJson.toString().toRequestBody(mediaType)

        val stkPushRequest = Request.Builder()
            .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
            .addHeader("Authorization", "Bearer $accessToken")
            .post(body)
            .build()

        val stkPushResponse = client.newCall(stkPushRequest).execute()

        // Print the response
        println(stkPushResponse.body?.string())
        stkPushResponse.body.let { responseBody ->
            val jsonObject = JSONObject(responseBody.string())
            return STKPushResponse(
                jsonObject.getString("MerchantRequestID"),
                jsonObject.getString("CheckoutRequestID"),
                jsonObject.getString("ResponseCode"),
                jsonObject.getString("ResponseDescription"),
                jsonObject.getString("CustomerMessage")
            )
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAccessToken(consumerKey: String, consumerSecret: String): String {
        val request = Request.Builder()
            .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
            .addHeader(
                "Authorization",
                "Basic " + Base64
                    .encode("$consumerKey:$consumerSecret".toByteArray())
            )
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body.string()
            val jsonObject = JSONObject(responseBody)
            return jsonObject.getString("access_token")
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}