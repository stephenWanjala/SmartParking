package com.github.parking.smartparking.home.data.remote

import com.github.parking.smartparking.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class AccessTokenInterceptor : Interceptor {

    @OptIn(ExperimentalEncodingApi::class)
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val keys = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET
        //Consumer key, consumer secret

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Basic " + Base64.encode((keys.toByteArray())))
            .build()
        return chain.proceed(request)
    }
}