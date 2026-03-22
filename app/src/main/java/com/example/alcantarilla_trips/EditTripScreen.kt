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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            Text(stringResource(R.string.rate_no_encontrado))
        }
        return
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    var title       by remember { mutableStateOf(trip.title) }
    var description by remember { mutableStateOf(trip.description) }
    var startDate   by remember { mutableStateOf<LocalDate?>(
        try { LocalDate.parse(trip.startDate, dateFormatter) } catch (e: Exception) { null }
    )}
    var endDate     by remember { mutableStateOf<LocalDate?>(
        try { LocalDate.parse(trip.endDate, dateFormatter) } catch (e: Exception) { null }
    )}

    var errorTitle     by remember { mutableStateOf<String?>(null) }
    var errorStartDate by remember { mutableStateOf<String?>(null) }
    var errorEndDate   by remember { mutableStateOf<String?>(null) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker   by remember { mutableStateOf(false) }
    var showSuccessDialog   by remember { mutableStateOf(false) }

    val strErrorTitulo     = stringResource(R.string.edit_trip_error_titulo)
    val strErrorFechaInicio = stringResource(R.string.edit_trip_error_fecha_inicio)
    val strErrorFechaFin   = stringResource(R.string.edit_trip_error_fecha_fin)
    val strErrorFechas     = stringResource(R.string.edit_trip_error_fechas)

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = startDate
                ?.atStartOfDay(java.time.ZoneId.systemDefault())
                ?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        startDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        errorStartDate = null
                    }
                    showStartDatePicker = false
                }) { Text(stringResource(R.string.aceptar)) }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text(stringResource(R.string.cancelar)) }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = endDate
                ?.atStartOfDay(java.time.ZoneId.systemDefault())
                ?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        endDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        errorEndDate = null
                    }
                    showEndDatePicker = false
                }) { Text(stringResource(R.string.aceptar)) }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text(stringResource(R.string.cancelar)) }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            icon = { Text("✅", style = MaterialTheme.typography.headlineLarge) },
            title = { Text(stringResource(R.string.edit_trip_exito_titulo), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
            text = { Text(stringResource(R.string.edit_trip_exito_texto)) },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false; navController.popBackStack() },
                    shape = RoundedCornerShape(12.dp)
                ) { Text(stringResource(R.string.volver), fontWeight = FontWeight.Bold) }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_trip_titulo), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.volver), tint = MaterialTheme.colorScheme.primary)
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
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorTitle = null },
                    label = { Text(stringResource(R.string.titulo_label)) },
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

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.descripcion_label)) },
                leadingIcon = { Icon(Icons.Default.Description, null, tint = MaterialTheme.colorScheme.primary) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )

            Column {
                OutlinedTextField(
                    value = startDate?.format(dateFormatter) ?: "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.edit_trip_fecha_inicio)) },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = if (errorStartDate != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
                    trailingIcon = {
                        IconButton(onClick = { showStartDatePicker = true }) {
                            Icon(Icons.Default.EditCalendar, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    readOnly = true,
                    isError = errorStartDate != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                AnimatedVisibility(visible = errorStartDate != null) {
                    Text(errorStartDate ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
                }
            }

            Column {
                OutlinedTextField(
                    value = endDate?.format(dateFormatter) ?: "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.edit_trip_fecha_fin)) },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = if (errorEndDate != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
                    trailingIcon = {
                        IconButton(onClick = { showEndDatePicker = true }) {
                            Icon(Icons.Default.EditCalendar, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    readOnly = true,
                    isError = errorEndDate != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                AnimatedVisibility(visible = errorEndDate != null) {
                    Text(errorEndDate ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(stringResource(R.string.edit_trip_info_vuelo), style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    Text("${trip.departureCity} → ${trip.destineCity}", fontWeight = FontWeight.Bold)
                    Text("${stringResource(R.string.itinerary_summary_vuelo)}: ${trip.flight}  ·  ${stringResource(R.string.itinerary_summary_hotel)}: ${trip.hotelName}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    var valid = true
                    if (title.isBlank()) { errorTitle = strErrorTitulo; valid = false }
                    if (startDate == null) { errorStartDate = strErrorFechaInicio; valid = false }
                    if (endDate == null) { errorEndDate = strErrorFechaFin; valid = false }
                    if (valid && startDate != null && endDate != null) {
                        if (!startDate!!.isBefore(endDate!!)) {
                            errorStartDate = strErrorFechas
                            valid = false
                        }
                    }
                    if (valid) {
                        viewModel.editTrip(
                            trip.copy(
                                title = title,
                                description = description,
                                startDate = startDate!!.format(dateFormatter),
                                endDate = endDate!!.format(dateFormatter)
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
                Text(stringResource(R.string.edit_trip_guardar), fontWeight = FontWeight.Bold)
            }
        }
    }
}