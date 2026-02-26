package com.example.alcantarilla_trips

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

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
    FlightResult("IB3456", "Iberia",   "2h 10m", 89, "Directo"),
    FlightResult("VY1234", "Vueling",  "2h 35m", 64, "Directo"),
    FlightResult("FR9821", "Ryanair",  "3h 05m", 41, "1 escala"),
    FlightResult("U23301", "easyJet",  "2h 50m", 55, "Directo"),
)

val mockHotels = listOf(
    HotelResult("Hotel Roquefort Palace", 5, 210, "Centro"),
    HotelResult("Madriguera Boutique",    4, 130, "0.5 km"),
    HotelResult("Cloaca Suites",          3,  78, "1.2 km"),
    HotelResult("Alcantarilla Inn",       3,  55, "2.0 km"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(navController: NavController) {

    var trip by remember { mutableStateOf(TripDraft()) }
    var currentStep by remember { mutableIntStateOf(0) }
    var flightsLoaded by remember { mutableStateOf(false) }
    var selectedFlight by remember { mutableStateOf<FlightResult?>(null) }
    var selectedHotel by remember { mutableStateOf<HotelResult?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val steps = listOf(
        stringResource(R.string.step_destino),
        stringResource(R.string.step_vuelo),
        stringResource(R.string.step_hotel),
        stringResource(R.string.step_fin)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.create_trip_titulo),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.create_trip_volver),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TripStepperBar(steps = steps, currentStep = currentStep)

                Box(modifier = Modifier.weight(1f)) {
                    when (currentStep) {
                        0 -> StepDestino(
                            trip = trip,
                            onTripChange = { trip = it },
                            onSearchFlights = { flightsLoaded = true; currentStep = 1 }
                        )
                        1 -> StepVuelo(
                            flights = if (flightsLoaded) mockFlights else emptyList(),
                            selectedFlight = selectedFlight,
                            onSelectFlight = { selectedFlight = it },
                            onContinue = {
                                trip = trip.copy(flight = selectedFlight?.code ?: "", price = selectedFlight?.price ?: 0)
                                currentStep = 2
                            }
                        )
                        2 -> StepHotel(
                            hotels = mockHotels,
                            selectedHotel = selectedHotel,
                            onSelectHotel = { selectedHotel = it },
                            onContinue = {
                                trip = trip.copy(hotelName = selectedHotel?.name ?: "")
                                currentStep = 3
                            }
                        )
                        3 -> StepResumen(
                            trip = trip,
                            selectedFlight = selectedFlight,
                            selectedHotel = selectedHotel,
                            onConfirm = { showSuccessDialog = true }
                        )
                    }
                }

                if (currentStep > 0) {
                    Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 8.dp) {
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                            OutlinedButton(
                                onClick = { currentStep-- },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = null)
                                Text(stringResource(R.string.create_trip_atras), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            icon = { Text("🎉", fontSize = 40.sp) },
            title = {
                Text(
                    stringResource(R.string.create_trip_exito_titulo),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    stringResource(R.string.create_trip_exito_texto, trip.destineCity),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate("home") {
                            popUpTo("create_trip") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.create_trip_exito_boton), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun TripStepperBar(steps: List<String>, currentStep: Int) {
    val primary   = MaterialTheme.colorScheme.primary
    val surface   = MaterialTheme.colorScheme.surfaceVariant
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val muted     = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 2.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, label ->
                val isDone    = index < currentStep
                val isCurrent = index == currentStep
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(28.dp).clip(CircleShape)
                            .background(when { isDone -> primary; isCurrent -> primary; else -> surface }),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = onPrimary, modifier = Modifier.size(16.dp))
                        } else {
                            Text("${index + 1}", style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isCurrent) onPrimary else muted))
                        }
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal, color = if (isCurrent) primary else muted))
                    if (index < steps.size - 1) {
                        Spacer(Modifier.width(4.dp))
                        Box(modifier = Modifier.width(18.dp).height(2.dp).background(if (isDone) primary else surface, RoundedCornerShape(1.dp)))
                    }
                }
            }
        }
    }
}

@Composable
fun StepDestino(trip: TripDraft, onTripChange: (TripDraft) -> Unit, onSearchFlights: () -> Unit) {
    var departureCityError by remember { mutableStateOf<String?>(null) }
    var destineCityError   by remember { mutableStateOf<String?>(null) }
    val errorOrigen  = stringResource(R.string.destino_error_origen)
    val errorDestino = stringResource(R.string.destino_error_destino)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            MapGridCanvas()
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🗺️", fontSize = 40.sp)
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.mapa_titulo), style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold))
                Text(stringResource(R.string.mapa_subtitulo), style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant), textAlign = TextAlign.Center)
            }
            if (trip.destineCity.isNotBlank()) {
                Box(modifier = Modifier.align(Alignment.Center).offset(x = 40.dp, y = (-20).dp)) {
                    TripMapPin(city = trip.destineCity)
                }
            }
        }

        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                Text("💡", fontSize = 16.sp)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(stringResource(R.string.mapa_integrar), style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold))
                    Text("implementation(\"com.google.maps.android:maps-compose:4.3.3\")", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }
        }

        TripSectionTitle(icon = Icons.Default.FlightTakeoff, title = stringResource(R.string.destino_de_donde))
        TripFormTextField(
            value = trip.departureCity,
            onValueChange = { onTripChange(trip.copy(departureCity = it)); departureCityError = null },
            label = stringResource(R.string.destino_ciudad_origen),
            placeholder = stringResource(R.string.destino_ciudad_origen_placeholder),
            leadingIcon = Icons.Default.MyLocation,
            isError = departureCityError != null,
            errorMessage = departureCityError
        )

        TripSectionTitle(icon = Icons.Default.FlightLand, title = stringResource(R.string.destino_a_donde))
        TripFormTextField(
            value = trip.destineCity,
            onValueChange = { onTripChange(trip.copy(destineCity = it)); destineCityError = null },
            label = stringResource(R.string.destino_ciudad_destino),
            placeholder = stringResource(R.string.destino_ciudad_destino_placeholder),
            leadingIcon = Icons.Default.LocationOn,
            isError = destineCityError != null,
            errorMessage = destineCityError
        )

        Button(
            onClick = {
                departureCityError = if (trip.departureCity.isBlank()) errorOrigen else null
                destineCityError   = if (trip.destineCity.isBlank()) errorDestino else null
                if (departureCityError == null && destineCityError == null) onSearchFlights()
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.destino_buscar_vuelos), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun MapGridCanvas() {
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 30.dp.toPx()
        var x = 0f
        while (x < size.width) { drawLine(gridColor, androidx.compose.ui.geometry.Offset(x, 0f), androidx.compose.ui.geometry.Offset(x, size.height), 1f); x += step }
        var y = 0f
        while (y < size.height) { drawLine(gridColor, androidx.compose.ui.geometry.Offset(0f, y), androidx.compose.ui.geometry.Offset(size.width, y), 1f); y += step }
    }
}

@Composable
fun TripMapPin(city: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primary, shadowElevation = 4.dp) {
            Text(city, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary))
        }
        Text("📍", fontSize = 20.sp)
    }
}

@Composable
fun StepVuelo(flights: List<FlightResult>, selectedFlight: FlightResult?, onSelectFlight: (FlightResult) -> Unit, onContinue: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TripSectionTitle(icon = Icons.Default.Flight, title = stringResource(R.string.vuelo_titulo))
        Text(stringResource(R.string.vuelo_opciones, flights.size), style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        flights.forEach { flight -> TripFlightCard(flight = flight, isSelected = flight == selectedFlight, onClick = { onSelectFlight(flight) }) }
        if (selectedFlight != null) {
            Spacer(Modifier.height(4.dp))
            Button(onClick = onContinue, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
                Text(stringResource(R.string.vuelo_continuar), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
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
                Text(stringResource(R.string.vuelo_por_rata), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            if (isSelected) { Spacer(Modifier.width(8.dp)); Icon(Icons.Default.CheckCircle, contentDescription = null, tint = primary, modifier = Modifier.size(20.dp)) }
        }
    }
}

@Composable
fun StepHotel(hotels: List<HotelResult>, selectedHotel: HotelResult?, onSelectHotel: (HotelResult) -> Unit, onContinue: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TripSectionTitle(icon = Icons.Default.Hotel, title = stringResource(R.string.hotel_titulo))
        hotels.forEach { hotel -> TripHotelCard(hotel = hotel, isSelected = hotel == selectedHotel, onClick = { onSelectHotel(hotel) }) }
        if (selectedHotel != null) {
            Spacer(Modifier.height(4.dp))
            Button(onClick = onContinue, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
                Text(stringResource(R.string.hotel_continuar), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }
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
                Text(stringResource(R.string.hotel_por_noche), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            if (isSelected) { Spacer(Modifier.width(8.dp)); Icon(Icons.Default.CheckCircle, contentDescription = null, tint = primary, modifier = Modifier.size(20.dp)) }
        }
    }
}

@Composable
fun StepResumen(trip: TripDraft, selectedFlight: FlightResult?, selectedHotel: HotelResult?, onConfirm: () -> Unit) {
    val totalPrice = (selectedFlight?.price ?: 0) + (selectedHotel?.price ?: 0)
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TripSectionTitle(icon = Icons.Default.Summarize, title = stringResource(R.string.resumen_titulo))
        TripSummaryRow(icon = "🛫", title = stringResource(R.string.resumen_ruta), value = "${trip.departureCity} → ${trip.destineCity}")
        if (selectedFlight != null)
            TripSummaryRow(icon = "✈️", title = "Vuelo · ${selectedFlight.code}", value = "${selectedFlight.airline} · ${selectedFlight.duration} · ${selectedFlight.stops}")
        if (selectedHotel != null)
            TripSummaryRow(icon = "🏨", title = stringResource(R.string.resumen_hotel), value = selectedHotel.name)
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)) {
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.resumen_precio), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("${totalPrice}€", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary))
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(14.dp), elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.resumen_confirmar), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black))
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
fun TripSectionTitle(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) },
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = keyboardOptions
        )
        AnimatedVisibility(visible = isError && errorMessage != null) {
            Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
        }
    }
}