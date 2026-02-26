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
fun About(navController: NavController) {
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
            Text("Sobre la app", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                modifier = Modifier.fillMaxWidth(0.85f),
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = """
🐀 Alcantarilla Trips v1.0

¿Cansado de cientos de apps para que seres humanos planifiquen sus viajes, mientras que tu colonia tiene que depender de papel de váter para hacer los apuntes?

¡Pues no más! Alcantarilla Trips es la primera app de viajes diseñada exclusivamente para roedores. Planifica tus expediciones subterráneas por los alcantarillados más populares, descubre nuevas rutas de queso y no te pierdas nunca más con tu camada.

🧀 CARACTERÍSTICAS
- **Planificación de Itinerarios** - Organiza tus viajes y visualiza tus rutas en segundos. 📅
- **Búsqueda de Lugares Cercanos** - Encuentra basureros, cocinas y más. 🧀
- **Almacenamiento de Imágenes** - Guarda recuerdos de tus viajes.
- **Filtros de riesgo avanzados** - Evalúa riesgo de humanos malhumorados en las zonas. 💀
- **Personalizar preferencias de usuario ⚙️**
- **Soporte para múltiples idiomas 🌎**

👥 EQUIPO
- Àlex Escofet Presedo
- Samuel Gomà Atehortua

📍 VERSIÓN
Alcantarilla Trips 1.0.0
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