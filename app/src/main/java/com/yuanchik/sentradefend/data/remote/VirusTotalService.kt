package com.yuanchik.sentradefend.data.remote

import com.yuanchik.sentradefend.di.VirusTotalApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VirusTotalService {
    private const val BASE_URL = "https://www.virustotal.com/api/v3/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: VirusTotalApi by lazy {
        retrofit.create(VirusTotalApi::class.java)
    }
}