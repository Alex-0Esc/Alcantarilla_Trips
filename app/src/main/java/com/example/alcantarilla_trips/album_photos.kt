package com.example.alcantarilla_trips

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

// ─── Modelo de foto ───────────────────────────────────────────────────
data class TripPhoto(
    val id: Int,
    val emoji: String,
    val city: String,
    val description: String,
    val date: String,
    val bgColor: Color
)

// ─── Fotos de muestra (en prod. usar Coil + URL real) ─────────────────
val samplePhotos = listOf(
    TripPhoto(1,  "🗼", "París",      "Atardecer en la Torre Eiffel",      "12 Mar 2025", Color(0xFFE8D5C4)),
    TripPhoto(2,  "🥐", "París",      "Petit déjeuner en Montmartre",      "13 Mar 2025", Color(0xFFF5E6D3)),
    TripPhoto(3,  "🗾", "Tokio",      "Barrio Shibuya de noche",           "28 Jun 2025", Color(0xFFD4E8F0)),
    TripPhoto(4,  "🍣", "Tokio",      "Sushi en el mercado Tsukiji",       "29 Jun 2025", Color(0xFFE0F0E8)),
    TripPhoto(5,  "🌸", "Tokio",      "Jardines del Palacio Imperial",     "30 Jun 2025", Color(0xFFF0E0E8)),
    TripPhoto(6,  "🏛️", "Roma",       "El Coliseo al amanecer",            "05 Ago 2025", Color(0xFFF0E8D4)),
    TripPhoto(7,  "🍕", "Roma",       "Pizza en Trastevere",               "06 Ago 2025", Color(0xFFE8F0D4)),
    TripPhoto(8,  "⛲", "Roma",       "Fontana di Trevi al mediodía",      "07 Ago 2025", Color(0xFFD4E4F0)),
    TripPhoto(9,  "🗽", "Nueva York", "Vista desde el Empire State",       "20 Nov 2025", Color(0xFFE4D4F0)),
    TripPhoto(10, "🌉", "Nueva York", "Brooklyn Bridge al atardecer",      "21 Nov 2025", Color(0xFFF0D4D4)),
)

// ─── Pantalla álbum de fotos ──────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoAlbumScreen(navController: NavController) {

    var currentIndex by remember { mutableIntStateOf(0) }
    val listState    = rememberLazyListState()
    val scope        = rememberCoroutineScope()
    val photo        = samplePhotos[currentIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Álbum de fotos",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            "${samplePhotos.size} recuerdos",
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
                }
            )
        },
        bottomBar = { AlbumBottomNav(navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(8.dp))

            // ── Foto principal ──────────────────────────────────────
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    (fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 4 }) togetherWith
                            (fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 4 })
                },
                label = "photoTransition"
            ) { idx ->
                val p = samplePhotos[idx]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(340.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(p.bgColor, p.bgColor.copy(alpha = 0.6f))
                            )
                        )
                        .pointerInput(idx) {
                            detectHorizontalDragGestures { _, dragAmount ->
                                if (dragAmount < -40 && currentIndex < samplePhotos.lastIndex) {
                                    currentIndex++
                                    scope.launch { listState.animateScrollToItem(currentIndex) }
                                } else if (dragAmount > 40 && currentIndex > 0) {
                                    currentIndex--
                                    scope.launch { listState.animateScrollToItem(currentIndex) }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Emoji grande simulando la foto
                    Text(p.emoji, fontSize = 110.sp)

                    // Degradado inferior con info
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                p.city,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Text(
                                p.date,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            )
                        }
                    }

                    // Contador arriba-derecha
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.35f)
                    ) {
                        Text(
                            "${idx + 1} / ${samplePhotos.size}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Descripción ─────────────────────────────────────────
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) },
                label = "descTransition"
            ) { idx ->
                Text(
                    samplePhotos[idx].description,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Flechas navegación ───────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            scope.launch { listState.animateScrollToItem(currentIndex) }
                        }
                    },
                    enabled = currentIndex > 0,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.ChevronLeft, "Anterior")
                }

                // Indicadores de puntos
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    samplePhotos.forEachIndexed { i, _ ->
                        val isSelected = i == currentIndex
                        val width by animateDpAsState(
                            targetValue = if (isSelected) 22.dp else 7.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "dotWidth"
                        )
                        Box(
                            modifier = Modifier
                                .height(7.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                        )
                    }
                }

                FilledTonalIconButton(
                    onClick = {
                        if (currentIndex < samplePhotos.lastIndex) {
                            currentIndex++
                            scope.launch { listState.animateScrollToItem(currentIndex) }
                        }
                    },
                    enabled = currentIndex < samplePhotos.lastIndex,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.ChevronRight, "Siguiente")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Tira de miniaturas ───────────────────────────────────
            Text(
                "Todas las fotos",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            Spacer(Modifier.height(10.dp))

            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(samplePhotos) { index, p ->
                    val isSelected = index == currentIndex
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.88f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "thumbScale"
                    )

                    Surface(
                        onClick = {
                            currentIndex = index
                            scope.launch { listState.animateScrollToItem(index) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .size(width = 72.dp, height = 84.dp),
                        color = p.bgColor,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(
                            2.5.dp, MaterialTheme.colorScheme.primary
                        ) else null,
                        shadowElevation = if (isSelected) 6.dp else 1.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(p.emoji, fontSize = 32.sp)
                        }
                    }
                }
            }
        }
    }
}

// ─── Bottom Nav compartido ────────────────────────────────────────────
@Composable
fun AlbumBottomNav(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("Itinerario",  Icons.Default.Book,   false),
            Triple("Album",  Icons.Default.Bookmark, false),
            Triple("Configuracion",  Icons.Default.Build, false)
        )
        items.forEachIndexed { index, (label, icon, selected) ->
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