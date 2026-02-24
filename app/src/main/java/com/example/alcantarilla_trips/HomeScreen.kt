package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.padding(50.dp))
            Text("¡Bienvenido Roedor!", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(25.dp))
            Text(text = "Qué quieres hacer hoy?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = { navController.navigate("create_trip") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Agendar nuevo viaje")
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("mis_viajes") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Ver mis viajes")
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("configuracion") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Configuración")
            }
        }
    }
}