package com.yuanchik.sentradefend.data

import com.google.gson.annotations.SerializedName

data class VirusScanResponse(
    @SerializedName("data") val data: Data
) {
    data class Data(
        @SerializedName("id") val id: String
    )
}

