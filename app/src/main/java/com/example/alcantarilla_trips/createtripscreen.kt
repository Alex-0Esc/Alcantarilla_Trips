package com.example.alcantarilla_trips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// ── Lista de ciudades ────────────────────────────────────────────────────────
private val CITIES = listOf(
    "London", "Barcelona", "Paris"
)

// ── Helpers fecha ────────────────────────────────────────────────────────────
private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

private fun LocalDate.toEpochMilli(): Long =
    atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()

data class TripDraft(
    val destineCity: String = "",
    val departureCity: String = "",
    val flight: String = "",
    val price: Int = 0,
    val hotelName: String = "",
    val imageId: String = ""
)

data class FlightResult(val code: String, val airline: String, val duration: String, val price: Int, val stops: String)
data class HotelResult(val name: String, val stars: Int, val price: Int, val distance: String)

val mockFlights = listOf(
    FlightResult("IB3456", "Iberia",  "2h 10m", 89, "Directo"),
    FlightResult("VY1234", "Vueling", "2h 35m", 64, "Directo"),
    FlightResult("FR9821", "Ryanair", "3h 05m", 41, "1 escala"),
    FlightResult("U23301", "easyJet", "2h 50m", 55, "Directo"),
)

val mockHotels = listOf(
    HotelResult("Hotel Roquefort Palace", 5, 210, "Centro"),
    HotelResult("Madriguera Boutique",    4, 130, "0.5 km"),
    HotelResult("Cloaca Suites",          3,  78, "1.2 km"),
    HotelResult("Alcantarilla Inn",       3,  55, "2.0 km"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    navController: NavController,
    viewModel: TripListViewModel = viewModel()
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val today = LocalDate.now()

    var trip              by remember { mutableStateOf(TripDraft()) }
    var flightsVisible    by remember { mutableStateOf(false) }
    var selectedFlight    by remember { mutableStateOf<FlightResult?>(null) }
    var selectedHotel     by remember { mutableStateOf<HotelResult?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Desplegables
    var expandDeparture  by remember { mutableStateOf(false) }
    var expandDestine    by remember { mutableStateOf(false) }

    // Errores
    var errorOrigen    by remember { mutableStateOf<String?>(null) }
    var errorDestino   by remember { mutableStateOf<String?>(null) }
    var errorStartDate by remember { mutableStateOf<String?>(null) }
    var errorEndDate   by remember { mutableStateOf<String?>(null) }

    // Fechas
    var startDate       by remember { mutableStateOf<LocalDate?>(null) }
    var endDate         by remember { mutableStateOf<LocalDate?>(null) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker   by remember { mutableStateOf(false) }

    val errorOrigenMsg      = stringResource(R.string.create_error_origen)
    val errorDestinoMsg     = stringResource(R.string.create_error_destino)
    val strErrorFechaInicio = stringResource(R.string.create_error_fecha_inicio)
    val strErrorFechaFin    = stringResource(R.string.create_error_fecha_fin)
    val strErrorFechas      = stringResource(R.string.create_error_fechas)
    val totalPrice = (selectedFlight?.price ?: 0) + (selectedHotel?.price ?: 0)

    // ── DatePicker: Fecha inicio (>= hoy) ────────────────────────────────────
    if (showStartPicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = startDate?.toEpochMilli() ?: today.toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) =
                    utcTimeMillis >= today.toEpochMilli()
            }
        )
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val picked = millis.toLocalDate()
                        startDate = picked
                        // Si la fecha fin queda antes, la reseteamos
                        if (endDate != null && endDate!! < picked) endDate = null
                        errorStartDate = null
                    }
                    showStartPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = state) }
    }

    // ── DatePicker: Fecha fin (>= startDate ?? hoy) ───────────────────────────
    if (showEndPicker) {
        val minEnd = startDate ?: today
        val state = rememberDatePickerState(
            initialSelectedDateMillis = endDate?.toEpochMilli() ?: minEnd.toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) =
                    utcTimeMillis >= minEnd.toEpochMilli()
            }
        )
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        endDate = millis.toLocalDate()
                        errorEndDate = null
                    }
                    showEndPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = state) }
    }

    // ════════════════════════════════════════════════════════════════════════
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.create_titulo),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.create_volver), tint = MaterialTheme.colorScheme.primary)
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SectionHeader(number = "1", icon = Icons.Default.FlightTakeoff, title = stringResource(R.string.create_seccion1_titulo))

            // ── Desplegable: Ciudad origen ────────────────────────────────────
            CityDropdown(
                value        = trip.departureCity,
                onSelect     = { trip = trip.copy(departureCity = it); errorOrigen = null },
                label        = stringResource(R.string.create_origen_label),
                leadingIcon  = Icons.Default.MyLocation,
                expanded     = expandDeparture,
                onExpandChange = { expandDeparture = it },
                isError      = errorOrigen != null,
                errorMessage = errorOrigen
            )

            // ── Desplegable: Ciudad destino ───────────────────────────────────
            CityDropdown(
                value        = trip.destineCity,
                onSelect     = { trip = trip.copy(destineCity = it); errorDestino = null },
                label        = stringResource(R.string.create_destino_label),
                leadingIcon  = Icons.Default.LocationOn,
                expanded     = expandDestine,
                onExpandChange = { expandDestine = it },
                isError      = errorDestino != null,
                errorMessage = errorDestino
            )

            // ── Fecha inicio ──────────────────────────────────────────────────
            DatePickerField(
                value        = startDate?.format(dateFormatter) ?: "",
                label        = stringResource(R.string.create_fecha_inicio),
                isError      = errorStartDate != null,
                errorMessage = errorStartDate,
                onPickerClick = { showStartPicker = true }
            )

            // ── Fecha fin ─────────────────────────────────────────────────────
            DatePickerField(
                value        = endDate?.format(dateFormatter) ?: "",
                label        = stringResource(R.string.create_fecha_fin),
                isError      = errorEndDate != null,
                errorMessage = errorEndDate,
                onPickerClick = { showEndPicker = true }
            )

            // ── Botón buscar vuelos ───────────────────────────────────────────
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    var valid = true

                    if (trip.departureCity.isBlank()) { errorOrigen = errorOrigenMsg;           valid = false }
                    if (trip.destineCity.isBlank())   { errorDestino = errorDestinoMsg;          valid = false }
                    if (startDate == null)             { errorStartDate = strErrorFechaInicio;    valid = false }
                    if (endDate == null)               { errorEndDate = strErrorFechaFin;         valid = false }

                    // startDate < hoy
                    if (startDate != null && startDate!! < today) {
                        errorStartDate = "La fecha de inicio no puede ser anterior a hoy"
                        valid = false
                    }
                    // endDate < startDate
                    if (startDate != null && endDate != null && endDate!! < startDate!!) {
                        errorEndDate = strErrorFechas
                        valid = false
                    }

                    if (valid) {
                        flightsVisible = true
                        selectedFlight = null
                        selectedHotel  = null
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Search, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.create_buscar_vuelos), fontWeight = FontWeight.Bold)
            }

            // ── Sección vuelos ────────────────────────────────────────────────
            AnimatedVisibility(visible = flightsVisible, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    SectionHeader(number = "2", icon = Icons.Default.Flight, title = stringResource(R.string.create_seccion2_titulo))
                    Text(
                        stringResource(R.string.create_vuelos_disponibles, mockFlights.size, trip.departureCity, trip.destineCity),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    mockFlights.forEach { flight ->
                        TripFlightCard(flight = flight, isSelected = flight == selectedFlight, onClick = { selectedFlight = flight })
                    }
                }
            }

            // ── Sección hoteles ───────────────────────────────────────────────
            AnimatedVisibility(visible = selectedFlight != null, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    SectionHeader(number = "3", icon = Icons.Default.Hotel, title = stringResource(R.string.create_seccion3_titulo))
                    mockHotels.forEach { hotel ->
                        TripHotelCard(hotel = hotel, isSelected = hotel == selectedHotel, onClick = { selectedHotel = hotel })
                    }
                }
            }

            // ── Sección resumen ───────────────────────────────────────────────
            AnimatedVisibility(visible = selectedHotel != null, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    SectionHeader(number = "4", icon = Icons.Default.Summarize, title = stringResource(R.string.create_seccion4_titulo))

                    TripSummaryRow(icon = "🛫", title = stringResource(R.string.create_resumen_ruta), value = "${trip.departureCity} → ${trip.destineCity}")
                    TripSummaryRow(icon = "📅", title = "Fechas", value = "${startDate?.format(dateFormatter)} → ${endDate?.format(dateFormatter)}")
                    selectedFlight?.let { f ->
                        TripSummaryRow(icon = "✈️", title = stringResource(R.string.create_resumen_vuelo, f.code), value = "${f.airline} · ${f.duration} · ${f.stops}")
                    }
                    selectedHotel?.let { h ->
                        TripSummaryRow(icon = "🏨", title = stringResource(R.string.create_resumen_hotel), value = h.name)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stringResource(R.string.create_precio_total), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text("${totalPrice}€", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary))
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Button(
                        onClick = {
                            viewModel.addTrip(
                                title         = "${trip.departureCity} → ${trip.destineCity}",
                                description   = "",
                                startDate     = startDate?.format(dateFormatter) ?: "",
                                endDate       = endDate?.format(dateFormatter) ?: "",
                                destineCity   = trip.destineCity,
                                departureCity = trip.departureCity,
                                flight        = selectedFlight?.code ?: "",
                                price         = totalPrice,
                                hotelName     = selectedHotel?.name ?: "",
                                imageEmoji    = "🏙️",
                                selectedHotel = selectedHotel,
                                selectedFlight = selectedFlight
                            )
                            showSuccessDialog = true
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.create_confirmar), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black))
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }

    // ── Diálogo éxito ─────────────────────────────────────────────────────────
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            icon = { Text("🎉", fontSize = 40.sp) },
            title = {
                Text(
                    stringResource(R.string.create_dialogo_titulo),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    stringResource(R.string.create_dialogo_texto, trip.destineCity),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate("mis_viajes") {
                            popUpTo("create_trip") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) { Text(stringResource(R.string.create_dialogo_boton), fontWeight = FontWeight.Bold) }
            }
        )
    }
}

// ── Composable: Desplegable de ciudades ──────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDropdown(
    value: String,
    onSelect: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandChange
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                leadingIcon = {
                    Icon(
                        leadingIcon, null,
                        tint = if (isError) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                isError = isError,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandChange(false) }
            ) {
                CITIES.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city) },
                        onClick = { onSelect(city); onExpandChange(false) }
                    )
                }
            }
        }
        AnimatedVisibility(visible = isError && errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp)
            )
        }
    }
}

// ── Composable: Campo de fecha (solo lectura + botón picker) ─────────────────
@Composable
fun DatePickerField(
    value: String,
    label: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    onPickerClick: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    Icons.Default.CalendarMonth, null,
                    tint = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                IconButton(onClick = onPickerClick) {
                    Icon(Icons.Default.EditCalendar, null, tint = MaterialTheme.colorScheme.primary)
                }
            },
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        AnimatedVisibility(visible = isError && errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp)
            )
        }
    }
}

// ── Los composables de abajo no cambian ──────────────────────────────────────

@Composable
fun SectionHeader(number: String, icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(30.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
            Text(number, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimary))
        }
        Spacer(Modifier.width(10.dp))
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(6.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun TripFlightCard(flight: FlightResult, isSelected: Boolean, onClick: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.then(if (isSelected) Modifier.border(2.dp, primary, RoundedCornerShape(16.dp)) else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) { Text("✈️", fontSize = 20.sp) }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("${flight.airline} · ${flight.code}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text("${flight.duration} · ${flight.stops}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${flight.price}€", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black, color = primary))
                Text(stringResource(R.string.create_vuelo_por_rata), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            if (isSelected) { Spacer(Modifier.width(8.dp)); Icon(Icons.Default.CheckCircle, null, tint = primary, modifier = Modifier.size(20.dp)) }
        }
    }
}

@Composable
fun TripHotelCard(hotel: HotelResult, isSelected: Boolean, onClick: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.then(if (isSelected) Modifier.border(2.dp, primary, RoundedCornerShape(16.dp)) else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) { Text("🏨", fontSize = 20.sp) }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(hotel.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(hotel.stars) { Text("⭐", fontSize = 10.sp) }
                    Spacer(Modifier.width(6.dp))
                    Text(hotel.distance, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${hotel.price}€", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black, color = primary))
                Text(stringResource(R.string.create_hotel_por_noche), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            if (isSelected) { Spacer(Modifier.width(8.dp)); Icon(Icons.Default.CheckCircle, null, tint = primary, modifier = Modifier.size(20.dp)) }
        }
    }
}

@Composable
fun TripSummaryRow(icon: String, title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
fun TripFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = { Icon(leadingIcon, null, tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
            singleLine = true, isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = keyboardOptions
        )
        AnimatedVisibility(visible = isError && errorMessage != null) {
            Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
        }
    }
}