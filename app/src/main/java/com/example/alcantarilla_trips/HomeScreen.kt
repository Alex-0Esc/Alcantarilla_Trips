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
            Text(stringResource(R.string.home_bienvenido), style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(25.dp))
            Text(stringResource(R.string.home_que_hacer), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = { navController.navigate("create_trip") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.home_agendar_viaje))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("mis_viajes") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.home_ver_viajes))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("configuracion") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(R.string.home_configuracion))
            }
        }
    }
}