package com.example.alcantarilla_trips.Configuracion
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
fun CambiarIdioma(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(), // Ocupa toda la pantalla
        horizontalAlignment = Alignment.CenterHorizontally, // Centra de izquierda a derecha
        verticalArrangement = Arrangement.Top // Los mantiene arriba
    ) {
        Spacer(modifier = Modifier.padding(50.dp)) // Espacio superior
        Text("Selecciona el idioma de la app", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.padding(25.dp))
        Button(onClick = { /*Acción para ver los viajes pendientes*/ }, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Español")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = { /*Acción para ver los viajes pendientes*/ }, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Catalan")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = { /*Acción para ver los viajes pendientes*/ }, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("English")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = { navController.navigate("configuracion")}, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Volver a configuracion")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = {navController.navigate("home")}, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Volver a la pantalla principal")
        }
    }
}