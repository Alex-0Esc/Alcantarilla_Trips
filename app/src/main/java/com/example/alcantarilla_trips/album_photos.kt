package com.example.alcantarilla_trips

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alcantarilla_trips.domain.TripImage
import com.example.alcantarilla_trips.ui.viewmodels.TripImageViewModel
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoAlbumScreen(
    navController: NavController,
    tripId: Int,
    imageViewModel: TripImageViewModel = viewModel(),
    tripViewModel: TripListViewModel = viewModel()
) {
    LaunchedEffect(tripId) { imageViewModel.loadImages(tripId) }

    val images by imageViewModel.images.collectAsState()
    val trip = tripViewModel.trips.collectAsState().value.find { it.tripId == tripId }

    var currentIndex by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf<TripImage?>(null) }

    // Clamp index when images shrink
    LaunchedEffect(images.size) {
        if (images.isNotEmpty() && currentIndex >= images.size) {
            currentIndex = images.size - 1
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageViewModel.addImage(tripId, it.toString()) }
    }

    showDeleteDialog?.let { img ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar foto") },
            text = { Text("¿Eliminar esta imagen del álbum?") },
            confirmButton = {
                TextButton(onClick = {
                    imageViewModel.deleteImage(img.imageId)
                    showDeleteDialog = null
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            trip?.title ?: stringResource(R.string.album_titulo),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            stringResource(R.string.album_recuerdos, images.size),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { imagePicker.launch("image/*") }) {
                        Icon(Icons.Default.AddPhotoAlternate, "Añadir foto", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (images.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("📷", fontSize = 64.sp)
                    Text(
                        "No hay fotos aún",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "Pulsa + para añadir fotos de este viaje",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        ),
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { imagePicker.launch("image/*") },
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Añadir foto", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))

                // Main image viewer
                AnimatedContent(
                    targetState = currentIndex,
                    transitionSpec = {
                        (fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 4 }) togetherWith
                                (fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 4 })
                    },
                    label = "photoTransition"
                ) { idx ->
                    val safeIdx = idx.coerceIn(0, images.lastIndex)
                    val img = images[safeIdx]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(340.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .pointerInput(safeIdx) {
                                detectHorizontalDragGestures { _, dragAmount ->
                                    if (dragAmount < -40 && currentIndex < images.lastIndex) {
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
                        AsyncImage(
                            model = img.uriString,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Counter badge
                        Surface(
                            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = Color.Black.copy(alpha = 0.35f)
                        ) {
                            Text(
                                "${safeIdx + 1} / ${images.size}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        // Delete button
                        IconButton(
                            onClick = { showDeleteDialog = img },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.35f), CircleShape)
                        ) {
                            Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Navigation controls
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
                    ) { Icon(Icons.Default.ChevronLeft, stringResource(R.string.album_anterior)) }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        images.forEachIndexed { i, _ ->
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
                            if (currentIndex < images.lastIndex) {
                                currentIndex++
                                scope.launch { listState.animateScrollToItem(currentIndex) }
                            }
                        },
                        enabled = currentIndex < images.lastIndex,
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) { Icon(Icons.Default.ChevronRight, stringResource(R.string.album_siguiente)) }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    stringResource(R.string.album_todas_fotos),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
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
                    itemsIndexed(images) { index, img ->
                        val isSelected = index == currentIndex
                        Surface(
                            onClick = {
                                currentIndex = index
                                scope.launch { listState.animateScrollToItem(index) }
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.size(width = 72.dp, height = 84.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) BorderStroke(2.5.dp, MaterialTheme.colorScheme.primary) else null,
                            shadowElevation = if (isSelected) 6.dp else 1.dp
                        ) {
                            AsyncImage(
                                model = img.uriString,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}