package com.yuanchik.sentradefend.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {

    // 1. Создаём логгер
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Показывает всё: запросы и ответы
    }

    // 2. Создаём общий OkHttpClient с логгером
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofitVT = Retrofit.Builder()
        .baseUrl("https://www.virustotal.com/api/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitFLP = Retrofit.Builder()
        .baseUrl("https://api.fraudlabspro.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val virusTotal: VirusTotalApi = retrofitVT.create(VirusTotalApi::class.java)
    val fraudLabs: FraudLabsApi = retrofitFLP.create(FraudLabsApi::class.java)

}