package com.yuanchik.sentradefend.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FraudLabsApi {
    @GET("v1/ip")
    suspend fun checkIp(
        @Query("ip") ip: String,
        @Query("key") key: String
    ): Response<Map<String, Any>>
}