package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Configuracion(navController: NavController, themeViewModel: ThemeViewModel) {
    val isDark by themeViewModel.isDarkTheme.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Spacer(modifier = Modifier.padding(50.dp))
            Text(stringResource(R.string.configuracion_titulo), style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(25.dp))

            Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.configuracion_modo_oscuro), style = MaterialTheme.typography.bodyLarge)
                Switch(checked = isDark, onCheckedChange = { themeViewModel.toggleTheme() })
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("cambiar_idioma") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.configuracion_cambiar_idioma))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("informacion") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.configuracion_informacion))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("login") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.configuracion_cerrar_sesion))
            }
        }
    }
}