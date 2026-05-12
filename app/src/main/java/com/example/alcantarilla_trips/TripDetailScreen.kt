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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alcantarilla_trips.ui.viewmodels.ActivityViewModel
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    navController: NavController,
    tripId: Int,
    tripViewModel: TripListViewModel = viewModel(),
    activityViewModel: ActivityViewModel = viewModel()
) {
    val trip = tripViewModel.trips.collectAsState().value.find { it.tripId == tripId }
    val activities by activityViewModel.activities.collectAsState()

    LaunchedEffect(tripId) {
        activityViewModel.loadActivities(tripId)
    }

    if (trip == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.rate_no_encontrado))
        }
        return
    }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trip.title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.volver), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallFloatingActionButton(
                    onClick = { navController.navigate("album/$tripId") },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(Icons.Default.PhotoLibrary, "Álbum de fotos")
                }
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("add_activity/$tripId") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text(stringResource(R.string.trip_detail_nueva_actividad), fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(trip.imageEmoji, fontSize = 32.sp)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("${trip.departureCity} → ${trip.destineCity}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text("${trip.startDate} → ${trip.endDate}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)))
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Flight, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                                Text(trip.flight, style = MaterialTheme.typography.bodySmall)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Hotel, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                                Text(trip.hotelName, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(stringResource(R.string.itinerary_titulo), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text("${activities.size} ${stringResource(R.string.trip_detail_actividades)}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }

            if (activities.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🗓️", fontSize = 48.sp)
                            Text(stringResource(R.string.trip_detail_sin_actividades), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text(stringResource(R.string.trip_detail_sin_actividades_sub), style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                }
            } else {
                items(activities, key = { it.activityId }) { activity ->
                    ActivityCard(
                        activity = activity,
                        onEdit = { navController.navigate("edit_activity/${activity.activityId}/$tripId") },
                        onDelete = { activityViewModel.deleteActivity(activity.activityId) }
                    )
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun ActivityCard(
    activity: com.example.alcantarilla_trips.domain.Activity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(activity.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    if (activity.description.isNotBlank()) {
                        Text(activity.description, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant), maxLines = 2)
                    }
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                    }
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(activity.date.format(dateFormatter), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Schedule, null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(activity.time.format(timeFormatter), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }
        }
    }
}