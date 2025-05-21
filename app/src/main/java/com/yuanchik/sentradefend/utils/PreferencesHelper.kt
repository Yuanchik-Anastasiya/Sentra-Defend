package com.yuanchik.sentradefend.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREF_NAME = "settings_pref"

    fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveBoolean(context: Context, key: String, value: Boolean) {
        getPreferences(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, default: Boolean = false): Boolean {
        return getPreferences(context).getBoolean(key, default)
    }
    fun saveString(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String): String {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString(key, "") ?: ""
    }
}