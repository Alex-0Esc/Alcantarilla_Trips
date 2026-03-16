package com.example.alcantarilla_trips.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LanguageViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(getSavedLanguage())
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private fun getSavedLanguage(): String {
        return prefs.getString("language", "es") ?: "es"
    }

    fun setLanguage(languageCode: String) {
        prefs.edit().putString("language", languageCode).apply()
        _currentLanguage.value = languageCode
        val appLocale = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun loadSavedLanguage() {
        val saved = getSavedLanguage()
        if (saved != "es") {
            val appLocale = LocaleListCompat.forLanguageTags(saved)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}