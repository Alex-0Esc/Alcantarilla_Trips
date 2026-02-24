package com.example.alcantarilla_trips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.alcantarilla_trips.ui.theme.Alcantarilla_TripsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Alcantarilla_TripsTheme {
                NavGraph()
            }
        }
    }
}

