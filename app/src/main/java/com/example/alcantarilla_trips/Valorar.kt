package com.example.alcantarilla_trips

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripStatus

val ratingTags = listOf(
    "🌟 Increíble",      "🍕 Buena comida",    "🏨 Hotel top",
    "✈️ Vuelo puntual",  "💸 Buen precio",     "🐭 Repetiría",
    "📸 Fotogénico",     "🚇 Buen transporte",  "🌦️ Mal tiempo",
    "😴 Agotador",       "🗺️ Muy turístico",   "🔥 Lo mejor"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateTripScreen(navController: NavController, tripId: Int) {

    val trip: Trip? = null

    if (trip == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("😵", fontSize = 48.sp)
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.rate_no_encontrado), style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(stringResource(R.string.rate_volver))
                }
            }
        }
        return
    }

    var selectedRating by remember { mutableIntStateOf(0) }
    var comment        by remember { mutableStateOf("") }
    var selectedTags   by remember { mutableStateOf(setOf<String>()) }
    var ratingError    by remember { mutableStateOf(false) }
    var submitted      by remember { mutableStateOf(false) }

    val ratingDesc = when (selectedRating) {
        1    -> stringResource(R.string.rate_desc_1)
        2    -> stringResource(R.string.rate_desc_2)
        3    -> stringResource(R.string.rate_desc_3)
        4    -> stringResource(R.string.rate_desc_4)
        5    -> stringResource(R.string.rate_desc_5)
        else -> stringResource(R.string.rate_desc_0)
    }

    val starScales = List(5) { i ->
        animateFloatAsState(
            targetValue = if (i < selectedRating) 1.25f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "star_$i"
        )
    }

    if (submitted) {
        SuccessScreen(trip = trip, rating = selectedRating, onBack = { navController.popBackStack() })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.rate_titulo), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.rate_volver), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.fillMaxSize().padding(padding), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                TripBanner(trip = trip)
                Column(modifier = Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Spacer(Modifier.height(4.dp))
                    RatingSection(
                        selectedRating = selectedRating,
                        ratingDesc = ratingDesc,
                        starScales = starScales.map { it.value },
                        ratingError = ratingError,
                        onStarClick = { index -> selectedRating = index + 1; ratingError = false }
                    )
                    TagsSection(selectedTags = selectedTags, onTagToggle = { tag ->
                        selectedTags = if (tag in selectedTags) selectedTags - tag else selectedTags + tag
                    })
                    CommentSection(comment = comment, onCommentChange = { if (it.length <= 400) comment = it })
                    Button(
                        onClick = { if (selectedRating == 0) ratingError = true else submitted = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text(stringResource(R.string.rate_publicar), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun TripBanner(trip: Trip) {
    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(brush = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))))) {
        Text(trip.imageEmoji, fontSize = 120.sp, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp).offset(y = 16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(24.dp)) {
            Text(trip.imageEmoji, fontSize = 44.sp)
            Spacer(Modifier.height(4.dp))
            Text(trip.destineCity, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${trip.departureCity} → ${trip.destineCity}", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)))
                Text("·", color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f))
                Text(trip.startDate, style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)))
            }
        }
    }
}

@Composable
fun RatingSection(selectedRating: Int, ratingDesc: String, starScales: List<Float>, ratingError: Boolean, onStarClick: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(R.string.rate_puntuacion), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
                repeat(5) { index ->
                    Text(text = if (index < selectedRating) "⭐" else "☆", fontSize = 44.sp, modifier = Modifier.scale(starScales[index]).clickable { onStarClick(index) })
                }
            }
            AnimatedContent(targetState = ratingDesc, transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) }, label = "ratingDesc") { desc ->
                Text(desc, style = MaterialTheme.typography.titleSmall.copy(
                    color = if (ratingError) MaterialTheme.colorScheme.error else if (selectedRating > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (selectedRating > 0) FontWeight.Bold else FontWeight.Normal
                ), textAlign = TextAlign.Center)
            }
            if (selectedRating > 0) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(5) { index ->
                        Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)).background(if (index < selectedRating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant))
                    }
                }
            }
            AnimatedVisibility(visible = ratingError) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(14.dp))
                    Text(stringResource(R.string.rate_error_puntuacion), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error))
                }
            }
        }
    }
}

@Composable
fun TagsSection(selectedTags: Set<String>, onTagToggle: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.LocalOffer, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Text(stringResource(R.string.rate_destacar), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(stringResource(R.string.rate_opcional), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            ratingTags.chunked(2).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { tag ->
                        val isSelected = tag in selectedTags
                        FilterChip(
                            selected = isSelected,
                            onClick = { onTagToggle(tag) },
                            label = { Text(tag, style = MaterialTheme.typography.labelMedium) },
                            modifier = Modifier.weight(1f),
                            leadingIcon = if (isSelected) { { Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp)) } } else null,
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), selectedLabelColor = MaterialTheme.colorScheme.primary, selectedLeadingIconColor = MaterialTheme.colorScheme.primary),
                            border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isSelected, selectedBorderColor = MaterialTheme.colorScheme.primary, borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), selectedBorderWidth = 1.5.dp)
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            AnimatedVisibility(visible = selectedTags.isNotEmpty()) {
                Text(
                    if (selectedTags.size == 1) stringResource(R.string.rate_etiqueta_singular, selectedTags.size)
                    else stringResource(R.string.rate_etiqueta_plural, selectedTags.size),
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun CommentSection(comment: String, onCommentChange: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.EditNote, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Text(stringResource(R.string.rate_opinion), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(stringResource(R.string.rate_opcional), style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            OutlinedTextField(
                value = comment,
                onValueChange = onCommentChange,
                placeholder = { Text(stringResource(R.string.rate_placeholder), style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
                shape = RoundedCornerShape(14.dp),
                maxLines = 8,
                supportingText = {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (comment.length > 350) stringResource(R.string.rate_casi_limite) else "", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error))
                        Text("${comment.length}/400", style = MaterialTheme.typography.labelSmall.copy(color = if (comment.length > 350) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                }
            )
        }
    }
}

@Composable
fun SuccessScreen(trip: Trip, rating: Int, onBack: () -> Unit) {
    val scale by animateFloatAsState(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "successScale")

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(120.dp).scale(scale).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Text("🌟", fontSize = 60.sp)
            }
            Spacer(Modifier.height(28.dp))
            Text(stringResource(R.string.rate_exito_titulo), style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary), textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            Text(stringResource(R.string.rate_exito_texto, trip.destineCity), style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)), textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(trip.imageEmoji, fontSize = 28.sp)
                    Text(trip.destineCity, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(Modifier.width(4.dp))
                    repeat(rating) { Text("⭐", fontSize = 18.sp) }
                }
            }
            Spacer(Modifier.height(40.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.ArrowBack, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.rate_exito_volver), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}