package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alcantarilla_trips.ui.viewmodels.LanguageViewModel

@Composable
fun CambiarIdioma(
    navController: NavController,
    languageViewModel: LanguageViewModel = viewModel()
) {
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()

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

            LanguageButton(
                label = stringResource(R.string.idioma_espanol),
                isSelected = currentLanguage == "es",
                onClick = { languageViewModel.setLanguage("es") }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            LanguageButton(
                label = stringResource(R.string.idioma_catalan),
                isSelected = currentLanguage == "ca",
                onClick = { languageViewModel.setLanguage("ca") }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            LanguageButton(
                label = stringResource(R.string.idioma_ingles),
                isSelected = currentLanguage == "en",
                onClick = { languageViewModel.setLanguage("en") }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            LanguageButton(
                label = "Português",
                isSelected = currentLanguage == "pt",
                onClick = { languageViewModel.setLanguage("pt") }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            OutlinedButton(
                onClick = { navController.navigate("configuracion") },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(stringResource(R.string.btn_volver_configuracion))
            }
        }
    }
}

@Composable
fun LanguageButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(0.8f),
        colors = if (isSelected)
            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        else
            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        if (isSelected) Text("✓  $label") else Text(label)
    }
}