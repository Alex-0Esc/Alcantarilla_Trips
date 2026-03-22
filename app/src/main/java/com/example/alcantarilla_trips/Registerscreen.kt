package com.example.alcantarilla_trips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun RegisterScreen(navController: NavController) {

    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible  by remember { mutableStateOf(false) }
    var acceptedTerms   by remember { mutableStateOf(false) }

    var emailError    by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError  by remember { mutableStateOf<String?>(null) }
    var termsError    by remember { mutableStateOf(false) }
    var isLoading     by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // Fortaleza de contraseña
    val passwordStrength = when {
        password.length >= 12 && password.any { it.isUpperCase() } && password.any { it.isDigit() } -> 3
        password.length >= 8  -> 2
        password.length >= 4  -> 1
        else                  -> 0
    }
    val strengthLabels = listOf("", "Débil", "Media", "Fuerte")
    val strengthColors = listOf(
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primary
    )

    fun validate(): Boolean {
        emailError    = if (email.isBlank() || !email.contains("@")) "Ingresa un email válido" else null
        passwordError = if (password.length < 6) "Mínimo 6 caracteres" else null
        confirmError  = if (password != confirmPassword) "Las contraseñas no coinciden" else null
        termsError    = !acceptedTerms
        return emailError == null && passwordError == null && confirmError == null && acceptedTerms
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Decoración fondo
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 80.dp, y = (-40).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(48.dp))

                // ── Logo ────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(28.dp),
                            ambientColor = RatTravelColors.YellowPrimary.copy(alpha = 0.4f),
                            spotColor = RatTravelColors.YellowPrimary.copy(alpha = 0.6f)
                        )
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(RatTravelColors.YellowLight, RatTravelColors.YellowDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = R.drawable.logo_animation,
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "Crea tu cuenta",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    "Únete a la comunidad de ratas viajeras",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(32.dp))

                // ── Tarjeta formulario ──────────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Datos de acceso",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        // ── Email ───────────────────────────────────
                        RegisterTextField(
                            value = email,
                            onValueChange = { email = it; emailError = null },
                            label = "Correo electrónico",
                            leadingIcon = Icons.Default.Email,
                            isError = emailError != null,
                            errorMessage = emailError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )

                        // ── Contraseña ──────────────────────────────
                        RegisterTextField(
                            value = password,
                            onValueChange = { password = it; passwordError = null },
                            label = "Contraseña",
                            leadingIcon = Icons.Default.Lock,
                            isError = passwordError != null,
                            errorMessage = passwordError,
                            visualTransformation = if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            trailingIcon = Icons.Default.Visibility,
                            trailingIconAlt = Icons.Default.VisibilityOff,
                            trailingIconToggle = passwordVisible,
                            onTrailingIconClick = { passwordVisible = !passwordVisible },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )

                        // Barra de fortaleza
                        AnimatedVisibility(visible = password.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    repeat(3) { index ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(
                                                    if (index < passwordStrength)
                                                        strengthColors[passwordStrength]
                                                    else
                                                        MaterialTheme.colorScheme.surfaceVariant
                                                )
                                        )
                                    }
                                }
                                if (passwordStrength > 0) {
                                    Text(
                                        "Contraseña ${strengthLabels[passwordStrength]}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = strengthColors[passwordStrength]
                                        )
                                    )
                                }
                            }
                        }

                        // ── Confirmar contraseña ────────────────────
                        RegisterTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it; confirmError = null },
                            label = "Confirmar contraseña",
                            leadingIcon = Icons.Default.LockOpen,
                            isError = confirmError != null,
                            errorMessage = confirmError,
                            visualTransformation = if (confirmVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            trailingIcon = Icons.Default.Visibility,
                            trailingIconAlt = Icons.Default.VisibilityOff,
                            trailingIconToggle = confirmVisible,
                            onTrailingIconClick = { confirmVisible = !confirmVisible },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            )
                        )

                        // ── Checkbox términos ───────────────────────
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = acceptedTerms,
                                    onCheckedChange = { acceptedTerms = it; termsError = false },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                        uncheckedColor = if (termsError) MaterialTheme.colorScheme.error
                                        else MaterialTheme.colorScheme.outline
                                    )
                                )
                                Spacer(Modifier.width(4.dp))
                                val termsText = buildAnnotatedString {
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 13.sp
                                        )
                                    ) { append("Acepto los ") }
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    ) { append("Términos y Condiciones") }
                                }
                                ClickableText(
                                    text = termsText,
                                    onClick = { navController.navigate("terminos") }
                                )
                            }
                            AnimatedVisibility(visible = termsError) {
                                Text(
                                    "Debes aceptar los términos para continuar",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.error
                                    ),
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }

                        // ── Botón crear cuenta ──────────────────────
                        Button(
                            onClick = {
                                if (validate()) {
                                    isLoading = true
                                    // TODO: lógica de registro real aquí
                                    navController.navigate("mis_viajes") {
                                        popUpTo("registro") { inclusive = true }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.5.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Icon(Icons.Default.PersonAdd, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Crear cuenta 🐾",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── Ya tengo cuenta ─────────────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "¿Ya tienes cuenta?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    )
                    Spacer(Modifier.width(6.dp))
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text(
                            "Inicia sesión",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// ─── TextField propio de Register (sin depender de LoginScreen) ───────
@Composable
fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    isError: Boolean = false,
    errorMessage: String? = null,
    // El trailing icon y la transformación son completamente opcionales
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: ImageVector? = null,
    trailingIconAlt: ImageVector? = null,
    trailingIconToggle: Boolean = false,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            },
            // Solo renderiza el trailing icon si se proporcionó
            trailingIcon = if (trailingIcon != null && onTrailingIconClick != null) {
                {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            imageVector = if (trailingIconToggle) (trailingIconAlt ?: trailingIcon)
                            else trailingIcon,
                            contentDescription = "Mostrar/ocultar",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            } else null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        AnimatedVisibility(visible = isError && errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp)
            )
        }
    }
}