package com.example.alcantarilla_trips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alcantarilla_trips.ui.viewmodels.AuthState
import com.example.alcantarilla_trips.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading
    val resetSent = authState is AuthState.PasswordResetSent

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar contraseña", color = RatTravelColors.OnSurface) },
                navigationIcon = {
                    IconButton(onClick = {
                        authViewModel.clearError()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, null, tint = RatTravelColors.YellowPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RatTravelColors.Background)
            )
        },
        containerColor = RatTravelColors.Background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            if (resetSent) {
                // --- Estado: email enviado ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = RatTravelColors.Surface)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MarkEmailRead,
                            contentDescription = null,
                            tint = RatTravelColors.YellowPrimary,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            "¡Correo enviado!",
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = RatTravelColors.OnSurface)
                        )
                        Text(
                            "Hemos enviado un enlace para restablecer tu contraseña a:\n$email\n\nRevisa tu bandeja de entrada y sigue las instrucciones.",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = RatTravelColors.OnSurfaceMuted,
                                textAlign = TextAlign.Center
                            )
                        )
                        Button(
                            onClick = {
                                authViewModel.clearError()
                                navController.navigate("login") {
                                    popUpTo("recuperar_password") { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RatTravelColors.YellowPrimary)
                        ) {
                            Text("Volver al inicio de sesión", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // --- Estado: formulario ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = RatTravelColors.Surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = RatTravelColors.YellowPrimary,
                            modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally)
                        )
                        Text(
                            "Introduce tu correo y te enviaremos un enlace para restablecer tu contraseña.",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = RatTravelColors.OnSurfaceMuted,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        RatTravelTextField(
                            value = email,
                            onValueChange = { email = it; emailError = null; authViewModel.clearError() },
                            label = "Correo electrónico",
                            leadingIcon = {
                                Icon(Icons.Default.Email, null,
                                    tint = if (emailError != null) RatTravelColors.ErrorRed else RatTravelColors.YellowPrimary)
                            },
                            isError = emailError != null,
                            errorMessage = emailError
                        )

                        // Error de Firebase
                        AnimatedVisibility(visible = authState is AuthState.Error) {
                            Text(
                                text = (authState as? AuthState.Error)?.message ?: "",
                                color = RatTravelColors.ErrorRed,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Button(
                            onClick = {
                                emailError = if (email.isBlank() || !email.contains("@"))
                                    "Correo inválido" else null
                                if (emailError == null) {
                                    authViewModel.sendPasswordReset(email)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RatTravelColors.YellowPrimary)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                            } else {
                                Text("Enviar enlace", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}