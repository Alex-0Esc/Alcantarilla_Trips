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
import androidx.navigation.NavController

// ─── Modelos ──────────────────────────────────────────────────────────
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

// ─── Pantalla única con scroll ────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(navController: NavController) {

    var trip           by remember { mutableStateOf(TripDraft()) }
    var flightsVisible by remember { mutableStateOf(false) }
    var selectedFlight by remember { mutableStateOf<FlightResult?>(null) }
    var selectedHotel  by remember { mutableStateOf<HotelResult?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Errores de validación
    var errorOrigen  by remember { mutableStateOf<String?>(null) }
    var errorDestino by remember { mutableStateOf<String?>(null) }

    val totalPrice = (selectedFlight?.price ?: 0) + (selectedHotel?.price ?: 0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Nuevo viaje",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ── SECCIÓN 1: Destino ───────────────────────────────────
            SectionHeader(
                number = "1",
                icon = Icons.Default.FlightTakeoff,
                title = "¿De dónde a dónde?"
            )

            TripFormTextField(
                value = trip.departureCity,
                onValueChange = { trip = trip.copy(departureCity = it); errorOrigen = null },
                label = "Ciudad de origen",
                placeholder = "Ej: Madrid",
                leadingIcon = Icons.Default.MyLocation,
                isError = errorOrigen != null,
                errorMessage = errorOrigen
            )

            TripFormTextField(
                value = trip.destineCity,
                onValueChange = { trip = trip.copy(destineCity = it); errorDestino = null },
                label = "Ciudad de destino",
                placeholder = "Ej: París",
                leadingIcon = Icons.Default.LocationOn,
                isError = errorDestino != null,
                errorMessage = errorDestino
            )

            Button(
                onClick = {
                    errorOrigen  = if (trip.departureCity.isBlank()) "Introduce la ciudad de origen" else null
                    errorDestino = if (trip.destineCity.isBlank()) "Introduce la ciudad de destino" else null
                    if (errorOrigen == null && errorDestino == null) {
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
                Text("Buscar vuelos", fontWeight = FontWeight.Bold)
            }

            // ── SECCIÓN 2: Vuelos (aparece tras buscar) ──────────────
            AnimatedVisibility(
                visible = flightsVisible,
                enter = fadeIn() + expandVertically(),
                exit  = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    SectionHeader(
                        number = "2",
                        icon = Icons.Default.Flight,
                        title = "Elige tu vuelo"
                    )
                    Text(
                        "${mockFlights.size} vuelos disponibles · ${trip.departureCity} → ${trip.destineCity}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    mockFlights.forEach { flight ->
                        TripFlightCard(
                            flight = flight,
                            isSelected = flight == selectedFlight,
                            onClick = { selectedFlight = flight }
                        )
                    }
                }
            }

            // ── SECCIÓN 3: Hoteles (aparece al seleccionar vuelo) ────
            AnimatedVisibility(
                visible = selectedFlight != null,
                enter = fadeIn() + expandVertically(),
                exit  = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    SectionHeader(
                        number = "3",
                        icon = Icons.Default.Hotel,
                        title = "Elige tu hotel"
                    )

                    mockHotels.forEach { hotel ->
                        TripHotelCard(
                            hotel = hotel,
                            isSelected = hotel == selectedHotel,
                            onClick = { selectedHotel = hotel }
                        )
                    }
                }
            }

            // ── SECCIÓN 4: Resumen y confirmar (aparece al seleccionar hotel) ──
            AnimatedVisibility(
                visible = selectedHotel != null,
                enter = fadeIn() + expandVertically(),
                exit  = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    SectionHeader(
                        number = "4",
                        icon = Icons.Default.Summarize,
                        title = "Resumen del viaje"
                    )

                    // Fila ruta
                    TripSummaryRow(
                        icon = "🛫",
                        title = "Ruta",
                        value = "${trip.departureCity} → ${trip.destineCity}"
                    )

                    // Fila vuelo seleccionado
                    selectedFlight?.let { f ->
                        TripSummaryRow(
                            icon = "✈️",
                            title = "Vuelo · ${f.code}",
                            value = "${f.airline} · ${f.duration} · ${f.stops}"
                        )
                    }

                    // Fila hotel seleccionado
                    selectedHotel?.let { h ->
                        TripSummaryRow(
                            icon = "🏨",
                            title = "Hotel",
                            value = h.name
                        )
                    }

                    // Precio total
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Precio total estimado",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "${totalPrice}€",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // Botón confirmar
                    Button(
                        onClick = {
                            trip = trip.copy(
                                flight   = selectedFlight?.code ?: "",
                                price    = totalPrice,
                                hotelName = selectedHotel?.name ?: ""
                            )
                            showSuccessDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Confirmar viaje 🐭",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }

    // ── Diálogo de éxito ─────────────────────────────────────────────
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            icon = { Text("🎉", fontSize = 40.sp) },
            title = {
                Text(
                    "¡Viaje creado!",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "Tu viaje a ${trip.destineCity} ha sido guardado correctamente.",
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
                ) {
                    Text("¡Vamos!", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

// ─── Cabecera de sección numerada ─────────────────────────────────────
@Composable
fun SectionHeader(number: String, icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                number,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
        Spacer(Modifier.width(10.dp))
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(6.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

// ─── Tarjeta de vuelo ─────────────────────────────────────────────────
@Composable
fun TripFlightCard(flight: FlightResult, isSelected: Boolean, onClick: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(2.dp, primary, RoundedCornerShape(16.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) { Text("✈️", fontSize = 20.sp) }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${flight.airline} · ${flight.code}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "${flight.duration} · ${flight.stops}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${flight.price}€",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = primary
                    )
                )
                Text(
                    "por rata",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            if (isSelected) {
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.CheckCircle, null, tint = primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// ─── Tarjeta de hotel ─────────────────────────────────────────────────
@Composable
fun TripHotelCard(hotel: HotelResult, isSelected: Boolean, onClick: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(2.dp, primary, RoundedCornerShape(16.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) { Text("🏨", fontSize = 20.sp) }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    hotel.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(hotel.stars) { Text("⭐", fontSize = 10.sp) }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        hotel.distance,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${hotel.price}€",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = primary
                    )
                )
                Text(
                    "por noche",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            if (isSelected) {
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.CheckCircle, null, tint = primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// ─── Fila de resumen ──────────────────────────────────────────────────
@Composable
fun TripSummaryRow(icon: String, title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    value,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// ─── Campo de texto ───────────────────────────────────────────────────
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
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    leadingIcon, null,
                    tint = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            },
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = keyboardOptions
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