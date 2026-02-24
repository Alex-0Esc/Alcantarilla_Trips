package com.example.alcantarilla_trips

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(3000L) // ajusta según duración de tu GIF
        onFinished()
    }

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // cambia el color de fondo
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.logo_animation) // pon el nombre exacto de tu gif
                .build(),
            imageLoader = imageLoader,
            contentDescription = "Splash",
            modifier = Modifier.size(200.dp)
        )
    }
}