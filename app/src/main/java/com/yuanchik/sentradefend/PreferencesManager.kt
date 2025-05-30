package com.yuanchik.sentradefend

import android.content.Context
import com.yuanchik.sentradefend.enum.AppTheme

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun saveTheme(theme: AppTheme) {
        prefs.edit().putInt("app_theme", theme.ordinal).apply()
    }

    fun getSavedTheme(): AppTheme {
        val ordinal = prefs.getInt("app_theme", AppTheme.SYSTEM.ordinal)
        return AppTheme.fromOrdinal(ordinal)
    }
}