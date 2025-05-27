package com.yuanchik.sentradefend.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREF_NAME = "settings_pref"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveBoolean(context: Context, key: String, value: Boolean) {
        getPreferences(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, default: Boolean = false): Boolean {
        return getPreferences(context).getBoolean(key, default)
    }

    fun saveString(context: Context, key: String, value: String) {
        getPreferences(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, default: String = ""): String {
        return getPreferences(context).getString(key, default) ?: default
    }
}