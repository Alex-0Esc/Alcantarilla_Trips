package com.example.alcantarilla_trips
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Bienvenida
        Text("¡Hola, viajero!", style = MaterialTheme.typography.headlineMedium)

        // 2. Buscador
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("¿A dónde quieres ir?") },
            modifier = Modifier.fillMaxWidth()
        )

        // 3. Destinos destacados
        Text("Destinos populares", style = MaterialTheme.typography.titleLarge)
        LazyRow { /* tarjetas */ }

        // 4. Mis viajes
        Text("Mis viajes", style = MaterialTheme.typography.titleLarge)
        LazyColumn { /* lista de viajes */ }

        // 5. CTA
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Planear nuevo viaje")
        }
    }
}