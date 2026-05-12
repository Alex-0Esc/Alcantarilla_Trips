package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alcantarilla_trips.domain.Booking
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.ui.viewmodels.HotelViewModel
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    navController: NavController,
    hotelViewModel: HotelViewModel = viewModel(),
    tripViewModel: TripListViewModel = viewModel()
) {
    val bookings by hotelViewModel.allBookings.collectAsState()
    val trips by tripViewModel.trips.collectAsState()
    var deleteTarget by remember { mutableStateOf<Booking?>(null) }

    deleteTarget?.let { booking ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            icon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Cancelar reserva") },
            text = { Text("¿Cancelar la reserva en ${booking.hotelName}? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        hotelViewModel.deleteBooking(booking.bookingId)
                        deleteTarget = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Cancelar reserva") }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) { Text("Volver") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mis reservas", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        Text("${bookings.size} reservas activas", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (bookings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(40.dp)
                ) {
                    Text("🏨", fontSize = 64.sp)
                    Text(
                        "No tienes reservas",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Las reservas de hotel aparecerán aquí",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bookings, key = { it.bookingId }) { booking ->
                    val relatedTrip = trips.find { it.tripId == booking.tripId }
                    BookingCard(
                        booking = booking,
                        relatedTrip = relatedTrip,
                        onDelete = { deleteTarget = booking },
                        onTripClick = {
                            relatedTrip?.let { navController.navigate("trip_detail/${it.tripId}") }
                        }
                    )
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun BookingCard(
    booking: Booking,
    relatedTrip: Trip?,
    onDelete: () -> Unit,
    onTripClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Hotel image
            if (booking.thumbnailUrl.isNotBlank()) {
                AsyncImage(
                    model = booking.thumbnailUrl,
                    contentDescription = booking.hotelName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏨", fontSize = 48.sp)
                }
            }

            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            booking.hotelName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            booking.city,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "%.0f€".format(booking.totalPrice),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            "total",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Room info
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Hotel, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(booking.roomName, style = MaterialTheme.typography.bodySmall)
                }

                // Dates
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.FlightLand, null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(booking.checkIn, style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.FlightTakeoff, null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(booking.checkOut, style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Payments, null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("%.0f€/noche".format(booking.pricePerNight), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                }

                // Related trip chip
                if (relatedTrip != null) {
                    Surface(
                        onClick = onTripClick,
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(relatedTrip.imageEmoji, fontSize = 12.sp)
                            Text(
                                relatedTrip.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(Icons.Default.OpenInNew, null, modifier = Modifier.size(10.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }

                // Delete button
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Cancel, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Cancelar reserva", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}