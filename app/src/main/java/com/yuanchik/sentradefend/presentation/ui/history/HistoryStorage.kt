package com.yuanchik.sentradefend.presentation.ui.history

import android.content.Context
import com.google.gson.Gson
import com.yuanchik.sentradefend.entity.ScanResultEntity

object HistoryStorage {
    private const val PREF_KEY = "scan_history"

    fun saveResult(context: Context, result: ScanResultEntity) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val existingJson = prefs.getString(PREF_KEY, "[]")
        val list = gson.fromJson(existingJson, Array<ScanResultEntity>::class.java).toMutableList()
        list.add(0, result)
        prefs.edit().putString(PREF_KEY, gson.toJson(list)).apply()
    }

    fun loadResults(context: Context): List<ScanResultEntity> {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(PREF_KEY, "[]")
        return gson.fromJson(json, Array<ScanResultEntity>::class.java).toList()
    }
}
