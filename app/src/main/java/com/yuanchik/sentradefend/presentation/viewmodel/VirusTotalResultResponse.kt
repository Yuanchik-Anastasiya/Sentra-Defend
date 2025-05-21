package com.yuanchik.sentradefend.presentation.viewmodel

import com.google.gson.annotations.SerializedName

data class VirusTotalResultResponse(
    val data: Data
) {
    data class Data(
        val attributes: Attributes
    ) {
        data class Attributes(
            @SerializedName("stats") val stats: Stats
        ) {
            data class Stats(
                val harmless: Int,
                val malicious: Int,
                val suspicious: Int,
                val undetected: Int
            )
        }
    }
}

