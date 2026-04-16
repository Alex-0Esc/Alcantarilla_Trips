package com.example.alcantarilla_trips

import android.util.Log
import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alcantarilla_trips.ui.viewmodels.AuthViewModel
import com.example.alcantarilla_trips.ui.viewmodels.AuthState

private const val TAG = "RegisterScreen"

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    // Navegación tras registro exitoso
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate("mis_viajes") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(RatTravelColors.Background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo circular según tu imagen
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(RatTravelColors.YellowPrimary),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = R.drawable.logo_animation, // Asegúrate de que existe
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crea tu cuenta",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = RatTravelColors.YellowPrimary)
            )
            Text(
                text = "Únete a la comunidad de ratas viajeras",
                style = TextStyle(fontSize = 14.sp, color = RatTravelColors.OnSurfaceMuted)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Card principal de "Datos de acceso"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = RatTravelColors.Surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Datos de acceso", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    // Campo Correo
                    RatTravelTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Correo electrónico",
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = RatTravelColors.YellowPrimary) }
                    )

                    // Campo Contraseña
                    RatTravelTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Contraseña",
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = RatTravelColors.YellowPrimary) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray)
                            }
                        }
                    )

                    // Barra de fortaleza (Visual como en tu imagen)
                    Row(Modifier.fillMaxWidth().height(4.dp).padding(horizontal = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(Modifier.weight(1f).fillMaxHeight().background(if(password.length > 3) Color.Red else Color.DarkGray, RoundedCornerShape(2.dp)))
                        Box(Modifier.weight(1f).fillMaxHeight().background(if(password.length > 6) Color.Yellow else Color.DarkGray, RoundedCornerShape(2.dp)))
                        Box(Modifier.weight(1f).fillMaxHeight().background(if(password.length > 9) Color.Green else Color.DarkGray, RoundedCornerShape(2.dp)))
                    }
                    if (password.isNotEmpty() && password.length < 6) {
                        Text("Contraseña Débil", color = Color.Red, fontSize = 10.sp)
                    }

                    // Confirmar Contraseña
                    RatTravelTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirmar contraseña",
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = RatTravelColors.YellowPrimary) },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray)
                            }
                        }
                    )

                    // Checkbox de Términos
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = acceptTerms,
                            onCheckedChange = { acceptTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = RatTravelColors.YellowPrimary)
                        )
                        Text("Acepto los ", color = Color.White, fontSize = 12.sp)
                        Text(
                            "Términos y Condiciones",
                            color = RatTravelColors.YellowPrimary,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { /* Navegar a términos */ }
                        )
                    }

                    // Manejo del error de Firebase (CONFIGURATION_NOT_FOUND)
                    if (authState is AuthState.Error) {
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = RatTravelColors.ErrorRed,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = { authViewModel.register(email, password) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = acceptTerms && !isLoading && password == confirmPassword,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RatTravelColors.YellowPrimary)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                        } else {
                            Icon(Icons.Default.PersonAdd, null, tint = Color.Black)
                            Spacer(Modifier.width(8.dp))
                            Text("Crear cuenta 🐾", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}