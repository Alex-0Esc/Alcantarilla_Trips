package com.example.alcantarilla_trips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.alcantarilla_trips.ui.theme.Alcantarilla_TripsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDark by themeViewModel.isDarkTheme.collectAsState()

            Alcantarilla_TripsTheme(darkTheme = isDark) {
                NavGraph(themeViewModel = themeViewModel)
            }
        }
    }
}