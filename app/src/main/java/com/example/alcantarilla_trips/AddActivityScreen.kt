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
import com.example.alcantarilla_trips.ui.viewmodels.ActivityViewModel
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    navController: NavController,
    tripId: Int,
    tripViewModel: TripListViewModel = viewModel(),
    activityViewModel: ActivityViewModel = viewModel()
) {
    val trip = tripViewModel.trips.collectAsState().value.find { it.tripId == tripId }
    val validationError by activityViewModel.validationError.collectAsState()

    var title       by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    var errorTitle by remember { mutableStateOf<String?>(null) }
    var errorDate  by remember { mutableStateOf<String?>(null) }
    var errorTime  by remember { mutableStateOf<String?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showSuccess   by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Parsear fechas del viaje para validación
    val tripStartDate = remember(trip) {
        try { trip?.startDate?.let { LocalDate.parse(it, dateFormatter) } } catch (e: Exception) { null }
    }
    val tripEndDate = remember(trip) {
        try { trip?.endDate?.let { LocalDate.parse(it, dateFormatter) } } catch (e: Exception) { null }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        errorDate = null
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Selecciona la hora") },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    errorTime = null
                    showTimePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            }
        )
    }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = {},
            icon = { Text("✅", style = MaterialTheme.typography.headlineLarge) },
            title = { Text("¡Actividad añadida!", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
            text = { Text("La actividad '$title' se ha añadido correctamente.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccess = false
                        navController.popBackStack()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Volver", fontWeight = FontWeight.Bold) }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva actividad", fontWeight = FontWeight.Bold) },
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
            // Info del viaje
            trip?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(it.imageEmoji)
                        Text("${it.departureCity} → ${it.destineCity}", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Text("${it.startDate} → ${it.endDate}", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)))
                    }
                }
            }

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

            // Fecha
            Column {
                OutlinedTextField(
                    value = selectedDate?.format(dateFormatter) ?: "",
                    onValueChange = {},
                    label = { Text("Fecha") },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = if (errorDate != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.EditCalendar, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    readOnly = true,
                    isError = errorDate != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                AnimatedVisibility(visible = errorDate != null) {
                    Text(errorDate ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
                }
            }

            // Hora
            Column {
                OutlinedTextField(
                    value = selectedTime?.format(timeFormatter) ?: "",
                    onValueChange = {},
                    label = { Text("Hora") },
                    leadingIcon = { Icon(Icons.Default.Schedule, null, tint = if (errorTime != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker = true }) {
                            Icon(Icons.Default.EditCalendar, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    readOnly = true,
                    isError = errorTime != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                AnimatedVisibility(visible = errorTime != null) {
                    Text(errorTime ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
                }
            }

            // Error de validación del ViewModel
            AnimatedVisibility(visible = validationError != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        Text(validationError ?: "", color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    var valid = true
                    if (title.isBlank()) { errorTitle = "El título no puede estar vacío"; valid = false }
                    if (selectedDate == null) { errorDate = "Selecciona una fecha"; valid = false }
                    if (selectedTime == null) { errorTime = "Selecciona una hora"; valid = false }

                    if (valid && selectedDate != null && selectedTime != null) {
                        val startDate = tripStartDate ?: LocalDate.MIN
                        val endDate = tripEndDate ?: LocalDate.MAX
                        activityViewModel.addActivity(
                            tripId = tripId,
                            title = title,
                            description = description,
                            date = selectedDate!!,
                            time = selectedTime!!,
                            tripStartDate = startDate,
                            tripEndDate = endDate
                        )
                        if (activityViewModel.validationError.value == null) {
                            showSuccess = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Añadir actividad", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}