package com.example.alcantarilla_trips

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.alcantarilla_trips.ui.theme.Alcantarilla_TripsTheme
import com.example.alcantarilla_trips.ui.viewmodels.LanguageViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDark by themeViewModel.isDarkTheme.collectAsState()
            val languageViewModel: LanguageViewModel = viewModel()

            // Carga el idioma guardado al arrancar
            languageViewModel.loadSavedLanguage()

            Alcantarilla_TripsTheme(darkTheme = isDark) {
                NavGraph(themeViewModel = themeViewModel)
            }
        }
    }
}