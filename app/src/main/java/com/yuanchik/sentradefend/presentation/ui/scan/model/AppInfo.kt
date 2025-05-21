package com.yuanchik.sentradefend.presentation.ui.scan.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    val apkPath: String,
    var isSelected: Boolean = false
)

