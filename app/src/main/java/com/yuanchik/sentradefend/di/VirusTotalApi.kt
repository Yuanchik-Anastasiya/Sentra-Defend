package com.yuanchik.sentradefend.di

import com.yuanchik.sentradefend.data.VirusScanResponse
import com.yuanchik.sentradefend.presentation.viewmodel.VirusTotalResultResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface VirusTotalApi {
    @FormUrlEncoded
    @POST("urls")
    suspend fun scanUrl(
        @Header("x-apikey") apiKey: String,
        @Field("url") url: String
    ): VirusScanResponse

    @GET("analyses/{id}")
    suspend fun getScanResult(
        @Header("x-apikey") apiKey: String,
        @Path("id") scanId: String
    ): VirusTotalResultResponse
}
