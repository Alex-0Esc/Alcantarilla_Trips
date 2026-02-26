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
fun Informacion(navController: NavController) {
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
            Text(stringResource(R.string.informacion_titulo), style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(25.dp))
            Button(onClick = { navController.navigate("terminos") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.informacion_terminos))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("about") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.informacion_about))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigateUp() }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.btn_volver))
            }
        }
    }
}