package com.yuanchik.sentradefend.di

import android.content.SharedPreferences
import com.yuanchik.sentradefend.presentation.ui.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getSharedPreferences(): SharedPreferences

    fun inject(fragment: SettingsFragment)
}