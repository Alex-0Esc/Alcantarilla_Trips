package com.example.alcantarilla_trips

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alcantarilla_trips.ui.viewmodels.SettingsViewModel
import com.example.alcantarilla_trips.ui.viewmodels.AuthViewModel
import com.example.alcantarilla_trips.ui.viewmodels.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Configuracion(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    settingsViewModel: SettingsViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel() // Hilt inyectará esto automáticamente
) {
    // Observamos el estado de autenticación del ViewModel
    val authState by authViewModel.authState.collectAsState()

    val isDark by themeViewModel.isDarkTheme.collectAsState()
    val username by settingsViewModel.username.collectAsState()
    val dateOfBirth by settingsViewModel.dateOfBirth.collectAsState()

    var usernameInput by remember(username) { mutableStateOf(username) }
    var dateOfBirthInput by remember(dateOfBirth) { mutableStateOf(dateOfBirth) }
    var showSavedSnackbar by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val strPreferenciasGuardadas = stringResource(R.string.configuracion_preferencias_guardadas)

    // EFECTO DE NAVEGACIÓN: Si el estado cambia a LoggedOut, salimos de la pantalla
    LaunchedEffect(authState) {
        if (authState is AuthState.LoggedOut) {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(showSavedSnackbar) {
        if (showSavedSnackbar) {
            snackbarHostState.showSnackbar(strPreferenciasGuardadas)
            showSavedSnackbar = false
        }
    }

    // --- DIÁLOGO DE SELECCIÓN DE FECHA ---
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        dateOfBirthInput = date.format(formatter)
                    }
                    showDatePicker = false
                }) { Text(stringResource(R.string.aceptar)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.cancelar)) }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    stringResource(R.string.configuracion_titulo),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))

                // --- TARJETA DE PERFIL ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(stringResource(R.string.configuracion_perfil), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                        OutlinedTextField(
                            value = usernameInput,
                            onValueChange = { usernameInput = it },
                            label = { Text(stringResource(R.string.configuracion_username)) },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = dateOfBirthInput,
                            onValueChange = {},
                            label = { Text(stringResource(R.string.configuracion_fecha_nacimiento)) },
                            leadingIcon = { Icon(Icons.Default.Cake, null, tint = MaterialTheme.colorScheme.primary) },
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.EditCalendar, null, tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = {
                                settingsViewModel.saveUsername(usernameInput)
                                settingsViewModel.saveDateOfBirth(dateOfBirthInput)
                                showSavedSnackbar = true
                                Log.d("Configuracion", "Preferencias de perfil guardadas localmente")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Save, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.configuracion_guardar_perfil), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- TARJETA DE APARIENCIA ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(stringResource(R.string.configuracion_apariencia), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.DarkMode, null, tint = MaterialTheme.colorScheme.primary)
                                Text(stringResource(R.string.configuracion_modo_oscuro), style = MaterialTheme.typography.bodyLarge)
                            }
                            Switch(checked = isDark, onCheckedChange = { themeViewModel.toggleTheme() })
                        }

                        Button(
                            onClick = { navController.navigate("cambiar_idioma") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Language, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.configuracion_cambiar_idioma), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- TARJETA DE INFORMACIÓN Y CIERRE DE SESIÓN ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(stringResource(R.string.informacion_titulo), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                        Button(
                            onClick = { navController.navigate("informacion") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Info, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.configuracion_informacion), fontWeight = FontWeight.Bold)
                        }

                        // T2.4 y T2.5: Implementación del Logout usando tu AuthViewModel
                        OutlinedButton(
                            onClick = { authViewModel.logout() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Logout, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.configuracion_cerrar_sesion), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}