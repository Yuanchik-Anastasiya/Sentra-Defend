package com.yuanchik.sentradefend.di

import android.content.Context
import android.content.SharedPreferences
import com.yuanchik.sentradefend.PreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferencesManager(): PreferencesManager {
        return PreferencesManager(context)
    }

}