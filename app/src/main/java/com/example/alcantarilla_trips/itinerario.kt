package com.example.alcantarilla_trips

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// ─── Modelos de itinerario ────────────────────────────────────────────
enum class ActivityType { FLIGHT, HOTEL, ACTIVITY, FOOD, TRANSPORT }

data class ItineraryActivity(
    val time: String,
    val title: String,
    val subtitle: String,
    val type: ActivityType,
    val duration: String = "",
    val note: String = ""
)

data class ItineraryDay(
    val dayNumber: Int,
    val date: String,
    val city: String,
    val emoji: String,
    val activities: List<ItineraryActivity>
)

// ─── Datos de muestra ────────────────────────────────────────────────
val sampleItinerary = listOf(
    ItineraryDay(
        dayNumber = 1, date = "20 Nov 2025", city = "Nueva York", emoji = "🗽",
        activities = listOf(
            ItineraryActivity("08:00", "Vuelo IB0091", "Valencia → Nueva York (JFK)", ActivityType.FLIGHT, "9h 30m"),
            ItineraryActivity("18:30", "Llegada a JFK", "Recogida de equipaje - Terminal 4", ActivityType.TRANSPORT, "45m"),
            ItineraryActivity("20:00", "Check-in Alcantarilla Inn", "5th Ave, Manhattan", ActivityType.HOTEL, "", "Habitación 402 reservada"),
            ItineraryActivity("21:30", "Cena en Times Square", "Junior's Restaurant", ActivityType.FOOD, "1h 30m"),
        )
    ),
    ItineraryDay(
        dayNumber = 2, date = "21 Nov 2025", city = "Nueva York", emoji = "🌆",
        activities = listOf(
            ItineraryActivity("09:00", "Desayuno en el hotel", "Buffet incluido", ActivityType.FOOD, "45m"),
            ItineraryActivity("10:30", "Empire State Building", "Subida al observatorio", ActivityType.ACTIVITY, "2h", "Entradas ya compradas"),
            ItineraryActivity("13:00", "Almuerzo en Midtown", "Shake Shack Madison Ave", ActivityType.FOOD, "1h"),
            ItineraryActivity("14:30", "Central Park", "Paseo y bicicleta", ActivityType.ACTIVITY, "2h 30m"),
            ItineraryActivity("19:00", "Brooklyn Bridge", "Paseo al atardecer", ActivityType.ACTIVITY, "1h"),
            ItineraryActivity("21:00", "Cena en DUMBO", "Time Out Market Brooklyn", ActivityType.FOOD, "1h 30m"),
        )
    ),
    ItineraryDay(
        dayNumber = 3, date = "22 Nov 2025", city = "Nueva York", emoji = "🗺️",
        activities = listOf(
            ItineraryActivity("09:30", "Museo MoMA", "Arte moderno y contemporáneo", ActivityType.ACTIVITY, "3h", "Reserva de 10:00 confirmada"),
            ItineraryActivity("13:00", "Almuerzo en Chelsea", "The High Line Food Market", ActivityType.FOOD, "1h"),
            ItineraryActivity("14:30", "High Line Park", "Paseo por el parque elevado", ActivityType.ACTIVITY, "1h 30m"),
            ItineraryActivity("17:00", "Compras en SoHo", "Tiendas y boutiques", ActivityType.ACTIVITY, "2h"),
            ItineraryActivity("20:00", "Cena de despedida", "Nobu Restaurant", ActivityType.FOOD, "2h"),
            ItineraryActivity("23:00", "Check-out preparación", "Maletas listas para mañana", ActivityType.HOTEL),
        )
    ),
    ItineraryDay(
        dayNumber = 4, date = "23 Nov 2025", city = "Regreso", emoji = "🏠",
        activities = listOf(
            ItineraryActivity("06:00", "Check-out hotel", "Recepción abierta 24h", ActivityType.HOTEL),
            ItineraryActivity("07:30", "Transfer al aeropuerto", "Taxi a JFK Terminal 4", ActivityType.TRANSPORT, "1h"),
            ItineraryActivity("11:00", "Vuelo IB0092", "Nueva York → Valencia", ActivityType.FLIGHT, "9h 15m"),
            ItineraryActivity("23:15", "Llegada a Valencia", "¡Bienvenido a casa! 🐭", ActivityType.TRANSPORT),
        )
    )
)

// ─── Pantalla de itinerario ───────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(navController: NavController) {

    var expandedDay by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Itinerario",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            "Nueva York · Nov 2025",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: compartir */ }) {
                        Icon(Icons.Default.Share, "Compartir", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        bottomBar = { ItineraryBottomNav(navController) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Cabecera resumen del viaje
            item {
                TripSummaryCard()
            }

            // Días del itinerario
            itemsIndexed(sampleItinerary) { index, day ->
                ItineraryDayCard(
                    day = day,
                    isExpanded = expandedDay == index,
                    isLast = index == sampleItinerary.lastIndex,
                    onToggle = { expandedDay = if (expandedDay == index) -1 else index }
                )
            }
        }
    }
}

// ─── Tarjeta resumen del viaje ────────────────────────────────────────
@Composable
fun TripSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(icon = "🗓️", value = "4", label = "Días")
            VerticalDivider(
                modifier = Modifier.height(40.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            )
            SummaryItem(icon = "🏙️", value = "1", label = "Ciudad")
            VerticalDivider(
                modifier = Modifier.height(40.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            )
            SummaryItem(icon = "✈️", value = "IB0091", label = "Vuelo")
            VerticalDivider(
                modifier = Modifier.height(40.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            )
            SummaryItem(icon = "🏨", value = "NYC Inn", label = "Hotel")
        }
    }
}

@Composable
fun SummaryItem(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 18.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            value,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            ),
            maxLines = 1
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
            )
        )
    }
}

// ─── Tarjeta de día con acordeón ─────────────────────────────────────
@Composable
fun ItineraryDayCard(
    day: ItineraryDay,
    isExpanded: Boolean,
    isLast: Boolean,
    onToggle: () -> Unit
) {
    Card(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isExpanded) 4.dp else 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Cabecera del día
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Número del día
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (isExpanded) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "D${day.dayNumber}",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = if (isExpanded) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(day.emoji, fontSize = 16.sp)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            day.city,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        day.date,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                // Chip con nº actividades
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        "${day.activities.size} actividades",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(Modifier.width(8.dp))

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Lista de actividades (expandible)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                exit  = shrinkVertically(tween(250)) + fadeOut(tween(200))
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    day.activities.forEachIndexed { index, activity ->
                        ActivityRow(
                            activity = activity,
                            isLast = index == day.activities.lastIndex
                        )
                    }
                }
            }
        }
    }
}

// ─── Fila de actividad con línea de tiempo ────────────────────────────
@Composable
fun ActivityRow(activity: ItineraryActivity, isLast: Boolean) {
    val (iconVector, iconColor) = activityIcon(activity.type)

    Row(modifier = Modifier.fillMaxWidth()) {

        // Línea de tiempo
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconVector, null, modifier = Modifier.size(16.dp), tint = iconColor)
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(28.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Contenido
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    activity.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    activity.time,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Text(
                activity.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (activity.duration.isNotEmpty() || activity.note.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (activity.duration.isNotEmpty()) {
                        MiniChip(
                            icon = Icons.Default.Timer,
                            text = activity.duration,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    if (activity.note.isNotEmpty()) {
                        MiniChip(
                            icon = Icons.Default.Info,
                            text = activity.note,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniChip(icon: ImageVector, text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(icon, null, modifier = Modifier.size(10.dp), tint = color)
            Text(
                text,
                style = MaterialTheme.typography.labelSmall.copy(color = color),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ─── Icono según tipo de actividad ────────────────────────────────────
@Composable
fun activityIcon(type: ActivityType): Pair<ImageVector, Color> = when (type) {
    ActivityType.FLIGHT    -> Icons.Default.Flight      to MaterialTheme.colorScheme.primary
    ActivityType.HOTEL     -> Icons.Default.Hotel       to MaterialTheme.colorScheme.tertiary
    ActivityType.ACTIVITY  -> Icons.Default.Explore     to Color(0xFF4CAF50)
    ActivityType.FOOD      -> Icons.Default.Restaurant  to Color(0xFFFF9800)
    ActivityType.TRANSPORT -> Icons.Default.DirectionsCar to MaterialTheme.colorScheme.secondary
}

// ─── Bottom Nav ───────────────────────────────────────────────────────
@Composable
fun ItineraryBottomNav(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("Itinerario",  Icons.Default.Book,   false),
            Triple("Album",  Icons.Default.Bookmark, false),
            Triple("Configuracion",  Icons.Default.Build, false)
        )
        items.forEachIndexed { _, (label, icon, selected) ->
            NavigationBarItem(
                selected = selected,
                onClick = { /* TODO */ },
                icon = { Icon(icon, label) },
                label = {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = MaterialTheme.colorScheme.primary,
                    selectedTextColor   = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    indicatorColor      = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}