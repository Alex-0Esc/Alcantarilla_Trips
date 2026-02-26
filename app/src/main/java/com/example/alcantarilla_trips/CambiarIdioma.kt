package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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
                stringResource(R.string.cambiar_idioma_titulo),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.padding(25.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.idioma_espanol))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.idioma_catalan))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.idioma_ingles))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("configuracion") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.btn_volver_configuracion))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("home") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.btn_volver_home))
            }
        }
    }
}