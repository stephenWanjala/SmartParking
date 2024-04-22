package com.github.parking.smartparking.home.domain.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale

object Utils {

    val timestamp: String
        get() = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

    fun sanitizePhoneNumber(phone: String): String {

        if (phone == "") {
            return ""
        }

        if ((phone.length < 11) and phone.startsWith("0")) {
            return phone.replaceFirst("^0".toRegex(), "254")
        }
        return if (phone.length == 13 && phone.startsWith("+")) {
            phone.replaceFirst("^+".toRegex(), "")
        } else phone
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPassword(businessShortCode: String, passkey: String, timestamp: String): String {
        val str = businessShortCode + passkey + timestamp
        //encode the password to Base64
        return Base64.getEncoder().encodeToString(str.toByteArray())
    }
}