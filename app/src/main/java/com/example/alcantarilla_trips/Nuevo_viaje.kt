package com.example.alcantarilla_trips
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.ButtonColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun Nuevo_viaje(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(), // Ocupa toda la pantalla
        horizontalAlignment = Alignment.CenterHorizontally, // Centra de izquierda a derecha
        verticalArrangement = Arrangement.Top // Los mantiene arriba
    ) {
        Spacer(modifier = Modifier.padding(50.dp)) // Espacio superior
        Text("¡Bienvenido Roedor!", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.padding(25.dp))
        Text(text = "Qué quieres hacer hoy?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.padding(20.dp))
        Button(onClick = { }, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Agendar nuevo viaje")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = { navController.navigate("agenda")}, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Ver viajes agendados")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = {navController.navigate("configuracion")}, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Configuración")
        }
    }
}