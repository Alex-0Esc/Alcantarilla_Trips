package com.example.alcantarilla_trips

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripStatus
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    navController: NavController,
    viewModel: TripListViewModel = viewModel()
) {
    val trips by viewModel.trips.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.trips_tab_pendientes) to Icons.Default.Schedule,
        stringResource(R.string.trips_tab_realizados) to Icons.Default.CheckCircle
    )

    val pendingTrips   = trips.filter { it.status == TripStatus.PENDING }
    val completedTrips = trips.filter { it.status == TripStatus.COMPLETED }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.trips_titulo),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("reservations") }) {
                        Icon(Icons.Default.BookOnline, "Reservas", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { navController.navigate("create_trip") }) {
                        Icon(Icons.Default.Add, stringResource(R.string.trips_nuevo_viaje), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.primary,
                            height = 3.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, (label, icon) ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(icon, null, modifier = Modifier.size(16.dp))
                                    Text(label, style = MaterialTheme.typography.labelLarge)
                                }
                            },
                            selectedContentColor   = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(300), initialOffsetX = { if (targetState > initialState) it else -it }) togetherWith
                                slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { if (targetState > initialState) -it else it })
                    },
                    label = "tabContent"
                ) { tab ->
                    val list = if (tab == 0) pendingTrips else completedTrips
                    if (list.isEmpty()) {
                        EmptyTripsState(
                            isPending = tab == 0,
                            onCreateTrip = { navController.navigate("create_trip") }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item { TripStatsRow(trips = list, isPending = tab == 0) }
                            items(list, key = { it.tripId }) { trip ->
                                TripCard(
                                    trip = trip,
                                    onClick = { navController.navigate("trip_detail/${trip.tripId}") },
                                    onDelete = { viewModel.deleteTrip(trip.tripId) },
                                    onEdit = { navController.navigate("edit_trip/${trip.tripId}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripStatsRow(trips: List<Trip>, isPending: Boolean) {
    val totalPrice = trips.sumOf { it.price }
    val cities     = trips.map { it.destineCity }.distinct().size
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        StatChip(modifier = Modifier.weight(1f), icon = if (isPending) "🗓️" else "✅", label = stringResource(R.string.trips_stat_viajes),   value = "${trips.size}")
        StatChip(modifier = Modifier.weight(1f), icon = "🌍",                            label = stringResource(R.string.trips_stat_ciudades), value = "$cities")
        StatChip(modifier = Modifier.weight(1f), icon = "💰",                            label = stringResource(R.string.trips_stat_total),    value = "${totalPrice}€")
    }
}

@Composable
fun StatChip(modifier: Modifier = Modifier, icon: String, label: String, value: String) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(modifier = Modifier.padding(10.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 18.sp)
            Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary))
            Text(label, style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}

@Composable
fun TripCard(trip: Trip, onClick: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    val statusColor = when (trip.status) {
        TripStatus.PENDING   -> MaterialTheme.colorScheme.tertiary
        TripStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        TripStatus.CANCELLED -> MaterialTheme.colorScheme.error
    }
    val statusLabel = when (trip.status) {
        TripStatus.PENDING   -> stringResource(R.string.trips_estado_pendiente)
        TripStatus.COMPLETED -> stringResource(R.string.trips_estado_realizado)
        TripStatus.CANCELLED -> stringResource(R.string.trips_estado_cancelado)
    }
    val statusIcon = when (trip.status) {
        TripStatus.PENDING   -> Icons.Default.Schedule
        TripStatus.COMPLETED -> Icons.Default.CheckCircle
        TripStatus.CANCELLED -> Icons.Default.Cancel
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(trip.imageEmoji, fontSize = 28.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(trip.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${trip.departureCity} → ${trip.destineCity}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(3.dp))
                        Text("${trip.startDate} → ${trip.endDate}", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${trip.price}€", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary))
                    Spacer(Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = statusColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(statusIcon, null, modifier = Modifier.size(10.dp), tint = statusColor)
                            Text(statusLabel, style = MaterialTheme.typography.labelSmall.copy(color = statusColor, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TripDetailChip(icon = Icons.Default.Flight, text = trip.flight,    modifier = Modifier.weight(1f))
                TripDetailChip(icon = Icons.Default.Hotel,  text = trip.hotelName, modifier = Modifier.weight(1f))
            }
            if (trip.hotelName.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.BookOnline, null, modifier = Modifier.size(11.dp), tint = MaterialTheme.colorScheme.onTertiaryContainer)
                        Text(
                            "Reserva activa",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            if (trip.status == TripStatus.PENDING) {
                Spacer(Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Cancel, null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.trips_btn_cancelar), style = MaterialTheme.typography.labelMedium)
                    }
                    Button(
                        onClick = onEdit,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Editar",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TripDetailChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.primary)
        Text(text, style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun EmptyTripsState(isPending: Boolean, onCreateTrip: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(if (isPending) "🧳" else "🗺️", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            if (isPending) stringResource(R.string.trips_vacio_pendiente_titulo) else stringResource(R.string.trips_vacio_realizado_titulo),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (isPending) stringResource(R.string.trips_vacio_pendiente_subtitulo) else stringResource(R.string.trips_vacio_realizado_subtitulo),
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onCreateTrip, shape = RoundedCornerShape(14.dp)) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.trips_btn_crear), fontWeight = FontWeight.Bold)
        }
    }
}