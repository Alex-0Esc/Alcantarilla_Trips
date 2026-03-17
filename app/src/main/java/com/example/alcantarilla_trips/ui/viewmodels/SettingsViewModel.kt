package com.example.alcantarilla_trips.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _username = MutableStateFlow(prefs.getString("username", "") ?: "")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _dateOfBirth = MutableStateFlow(prefs.getString("date_of_birth", "") ?: "")
    val dateOfBirth: StateFlow<String> = _dateOfBirth.asStateFlow()

    fun saveUsername(value: String) {
        _username.value = value
        prefs.edit().putString("username", value).apply()
    }

    fun saveDateOfBirth(value: String) {
        _dateOfBirth.value = value
        prefs.edit().putString("date_of_birth", value).apply()
    }
}