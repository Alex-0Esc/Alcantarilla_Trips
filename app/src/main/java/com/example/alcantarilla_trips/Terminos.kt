package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

            Text(
                text = stringResource(R.string.terminos_titulo),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(0.85f),
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(R.string.terminos_contenido)
            )

            Spacer(modifier = Modifier.padding(20.dp))

            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(stringResource(R.string.volver))
            }

            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}