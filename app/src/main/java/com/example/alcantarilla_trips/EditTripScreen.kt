package com.example.alcantarilla_trips

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripScreen(
    navController: NavController,
    tripId: Int,
    viewModel: TripListViewModel = viewModel()
) {
    val trip = viewModel.trips.collectAsState().value.find { it.tripId == tripId }

    if (trip == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Viaje no encontrado")
        }
        return
    }

    var title       by remember { mutableStateOf(trip.title) }
    var description by remember { mutableStateOf(trip.description) }
    var startDate   by remember { mutableStateOf(trip.startDate) }
    var endDate     by remember { mutableStateOf(trip.endDate) }

    var errorTitle     by remember { mutableStateOf<String?>(null) }
    var errorStartDate by remember { mutableStateOf<String?>(null) }
    var errorEndDate   by remember { mutableStateOf<String?>(null) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar viaje", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorTitle = null },
                    label = { Text("Título") },
                    leadingIcon = { Icon(Icons.Default.Title, null, tint = if (errorTitle != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
                    isError = errorTitle != null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                AnimatedVisibility(visible = errorTitle != null) {
                    Text(errorTitle ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
                }
            }

            // Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                leadingIcon = { Icon(Icons.Default.Description, null, tint = MaterialTheme.colorScheme.primary) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )

            // Fecha inicio
            Column {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it; errorStartDate = null },
                    label = { Text("Fecha de inicio (dd/MM/yyyy)") },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = if (errorStartDate != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
                    isError = errorStartDate != null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                AnimatedVisibility(visible = errorStartDate != null) {
                    Text(errorStartDate ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
                }
            }

            // Fecha fin
            Column {
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it; errorEndDate = null },
                    label = { Text("Fecha de fin (dd/MM/yyyy)") },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = if (errorEndDate != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
                    isError = errorEndDate != null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                AnimatedVisibility(visible = errorEndDate != null) {
                    Text(errorEndDate ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
                }
            }

            Spacer(Modifier.height(8.dp))

            // Info del viaje (no editable)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Información del vuelo", style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    Text("${trip.departureCity} → ${trip.destineCity}", fontWeight = FontWeight.Bold)
                    Text("Vuelo: ${trip.flight}  ·  Hotel: ${trip.hotelName}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    var valid = true
                    if (title.isBlank()) { errorTitle = "El título no puede estar vacío"; valid = false }
                    if (startDate.isBlank()) { errorStartDate = "La fecha de inicio es obligatoria"; valid = false }
                    if (endDate.isBlank()) { errorEndDate = "La fecha de fin es obligatoria"; valid = false }
                    if (valid) {
                        viewModel.editTrip(
                            trip.copy(
                                title = title,
                                description = description,
                                startDate = startDate,
                                endDate = endDate
                            )
                        )
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Save, null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar cambios", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            icon = { Text("✅", style = MaterialTheme.typography.headlineLarge) },
            title = { Text("¡Viaje actualizado!", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
            text = { Text("Los cambios en '$title' se han guardado correctamente.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Volver", fontWeight = FontWeight.Bold) }
            }
        )
    }
}
