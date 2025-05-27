package com.yuanchik.sentradefend.enum

import androidx.appcompat.app.AppCompatDelegate

enum class AppTheme(val mode: Int) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    DARK(AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        fun fromOrdinal(ordinal: Int): AppTheme {
            return values().getOrElse(ordinal) { SYSTEM }
        }
    }
}
