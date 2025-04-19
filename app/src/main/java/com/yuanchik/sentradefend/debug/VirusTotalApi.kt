package com.yuanchik.sentradefend.debug

import retrofit2.Response
import retrofit2.http.*

interface VirusTotalApi {
    @FormUrlEncoded
    @POST("urls")
    suspend fun scanUrl(
        @Header("x-apikey") apiKey: String,
        @Field("url") url: String,
    ): Response<Map<String, Any>>
}