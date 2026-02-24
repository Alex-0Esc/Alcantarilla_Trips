package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Terminos(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.padding(50.dp))
            Text("Términos y Condiciones", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                modifier = Modifier.fillMaxWidth(0.85f),
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = """
Al utilizar Alcantarilla Trips, el usuario (en adelante "el Roedor") acepta los siguientes términos:

1. USO DE LA APP
Alcantarilla Trips está diseñada exclusivamente para la planificación de viajes subterráneos. Cualquier uso en superficie queda bajo responsabilidad del Roedor.

2. RESPONSABILIDAD
No nos hacemos responsables de pérdidas de queso, encuentros con desagradables bípedos, ni de corrientes de agua inesperadas durante los viajes planificados con esta app.

3. PRIVACIDAD
Los datos de tus rutas de alcantarilla son estrictamente confidenciales y jamás serán compartidos con exteriores, gatos, ni autoridades sanitarias.

4. MODIFICACIONES
Alcantarilla Trips se reserva el derecho de modificar estas condiciones en cualquier momento. Los cambios serán comunicados mediante arañazos en las paredes del túnel principal.

5. ACEPTACIÓN
El simple hecho de olfatear esta pantalla implica la aceptación total de estos términos.
                """.trimIndent()
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = { navController.navigateUp() }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Volver")
            }
            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}