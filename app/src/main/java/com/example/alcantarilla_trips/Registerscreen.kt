package com.example.alcantarilla_trips

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

    // Validaciones locales
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    // T3.2: Si el registro fue exitoso y el email fue enviado, mostramos pantalla de confirmación
    if (authState is AuthState.EmailVerificationSent) {
        EmailVerificationSentScreen(
            email = email,
            onBackToLogin = {
                authViewModel.clearError()
                navController.navigate("login") {
                    popUpTo("registro") { inclusive = true }
                }
            }
        )
        return
    }

    fun validate(): Boolean {
        emailError = if (email.isBlank() || !email.contains("@")) "Correo inválido" else null
        passwordError = if (password.length < 6) "Mínimo 6 caracteres" else null
        confirmPasswordError = if (password != confirmPassword) "Las contraseñas no coinciden" else null
        return emailError == null && passwordError == null && confirmPasswordError == null
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

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(RatTravelColors.YellowPrimary),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = R.drawable.logo_animation,
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = RatTravelColors.Surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Datos de acceso", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    // Email
                    RatTravelTextField(
                        value = email,
                        onValueChange = { email = it; emailError = null; authViewModel.clearError() },
                        label = "Correo electrónico",
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = if (emailError != null) RatTravelColors.ErrorRed else RatTravelColors.YellowPrimary) },
                        isError = emailError != null,
                        errorMessage = emailError
                    )

                    // Contraseña
                    RatTravelTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = null; authViewModel.clearError() },
                        label = "Contraseña",
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = if (passwordError != null) RatTravelColors.ErrorRed else RatTravelColors.YellowPrimary) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray)
                            }
                        },
                        isError = passwordError != null,
                        errorMessage = passwordError
                    )

                    // Barra de fortaleza
                    PasswordStrengthBar(password = password)

                    // Confirmar contraseña
                    RatTravelTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; confirmPasswordError = null },
                        label = "Confirmar contraseña",
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = if (confirmPasswordError != null) RatTravelColors.ErrorRed else RatTravelColors.YellowPrimary) },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray)
                            }
                        },
                        isError = confirmPasswordError != null,
                        errorMessage = confirmPasswordError
                    )

                    // Checkbox términos
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
                            modifier = Modifier.clickable { navController.navigate("terminos") }
                        )
                    }

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

                    // Botón registrar
                    Button(
                        onClick = {
                            if (validate()) {
                                authViewModel.register(email, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = acceptTerms && !isLoading,
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

                    // Link a recuperar contraseña
                    TextButton(
                        onClick = { navController.navigate("recuperar_password") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "¿Ya tienes cuenta? Inicia sesión",
                            color = RatTravelColors.OnSurfaceMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- Pantalla de confirmación de email enviado (T3.2) ---
@Composable
fun EmailVerificationSentScreen(
    email: String,
    onBackToLogin: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(RatTravelColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = RatTravelColors.Surface)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = RatTravelColors.YellowPrimary,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    "¡Verifica tu correo!",
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = RatTravelColors.OnSurface)
                )
                Text(
                    "Hemos enviado un enlace de verificación a:\n$email\n\nRevisa tu bandeja de entrada (y spam) y haz clic en el enlace antes de iniciar sesión.",
                    style = TextStyle(fontSize = 14.sp, color = RatTravelColors.OnSurfaceMuted, textAlign = TextAlign.Center)
                )
                Button(
                    onClick = onBackToLogin,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RatTravelColors.YellowPrimary)
                ) {
                    Text("Ir al inicio de sesión", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- Barra de fortaleza de contraseña ---
@Composable
fun PasswordStrengthBar(password: String) {
    val strength = when {
        password.length >= 10 -> 3
        password.length >= 6  -> 2
        password.isNotEmpty() -> 1
        else                  -> 0
    }
    val label = when (strength) {
        3    -> Pair("Fuerte", Color.Green)
        2    -> Pair("Media", Color.Yellow)
        1    -> Pair("Débil", Color.Red)
        else -> Pair("", Color.Transparent)
    }
    if (password.isNotEmpty()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                Modifier.fillMaxWidth().height(4.dp).padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(1, 2, 3).forEach { level ->
                    Box(
                        Modifier.weight(1f).fillMaxHeight().background(
                            if (strength >= level) label.second else Color.DarkGray,
                            RoundedCornerShape(2.dp)
                        )
                    )
                }
            }
            Text(label.first, color = label.second, fontSize = 10.sp, modifier = Modifier.padding(start = 4.dp))
        }
    }
}