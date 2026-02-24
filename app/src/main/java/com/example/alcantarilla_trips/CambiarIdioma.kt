package com.example.alcantarilla_trips
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.compose.material3.Surface

@Composable
fun CambiarIdioma(navController: NavController) {
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

            Text(
                "Selecciona el idioma de la app",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.padding(25.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Español")
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Català")
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("English")
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("configuracion") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Volver a configuración")
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("home") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Volver a la pantalla principal")
            }
        }
    }
}