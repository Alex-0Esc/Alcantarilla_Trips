package com.example.alcantarilla_trips

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
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

object RatTravelColors {
    val YellowPrimary  = Color(0xFFFFC107)
    val YellowLight    = Color(0xFFFFE082)
    val YellowDark     = Color(0xFFF9A825)
    val YellowAccent   = Color(0xFFFFD54F)
    val Background     = Color(0xFF1A1A1A)
    val Surface        = Color(0xFF252525)
    val SurfaceVariant = Color(0xFF2E2E2E)
    val OnSurface      = Color(0xFFF5F5F5)
    val OnSurfaceMuted = Color(0xFF9E9E9E)
    val ErrorRed       = Color(0xFFEF5350)
}

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val errorEmailInvalido = stringResource(R.string.login_error_email)
    val errorPasswordCorta = stringResource(R.string.login_error_password)

    fun validateAndLogin() {
        emailError    = if (email.isBlank() || !email.contains("@")) errorEmailInvalido else null
        passwordError = if (password.length < 6) errorPasswordCorta else null
        if (emailError == null && passwordError == null) {
            isLoading = true
            navController.navigate("mis_viajes") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(RatTravelColors.Background)
    ) {
        Box(
            modifier = Modifier.size(320.dp).offset(x = (-80).dp, y = (-60).dp)
                .background(brush = Brush.radialGradient(colors = listOf(RatTravelColors.YellowPrimary.copy(alpha = 0.15f), Color.Transparent)), shape = CircleShape)
        )
        Box(
            modifier = Modifier.size(240.dp).align(Alignment.BottomEnd).offset(x = 60.dp, y = 60.dp)
                .background(brush = Brush.radialGradient(colors = listOf(RatTravelColors.YellowDark.copy(alpha = 0.12f), Color.Transparent)), shape = CircleShape)
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Box(
                modifier = Modifier.size(110.dp)
                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(28.dp), ambientColor = RatTravelColors.YellowPrimary.copy(alpha = 0.4f), spotColor = RatTravelColors.YellowPrimary.copy(alpha = 0.6f))
                    .clip(RoundedCornerShape(28.dp))
                    .background(brush = Brush.linearGradient(colors = listOf(RatTravelColors.YellowLight, RatTravelColors.YellowDark))),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = R.drawable.logo_animation,
                    contentDescription = stringResource(R.string.login_logo_desc),
                    modifier = Modifier.size(90.dp).clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = TextStyle(fontSize = 38.sp, fontWeight = FontWeight.Black, color = RatTravelColors.YellowPrimary, letterSpacing = (-1).sp)
            )
            Text(
                text = stringResource(R.string.login_subtitulo),
                style = TextStyle(fontSize = 14.sp, color = RatTravelColors.OnSurfaceMuted),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = RatTravelColors.Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(stringResource(R.string.login_titulo), style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RatTravelColors.OnSurface))

                    RatTravelTextField(
                        value = email,
                        onValueChange = { email = it; emailError = null },
                        label = stringResource(R.string.login_email),
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = if (emailError != null) RatTravelColors.ErrorRed else RatTravelColors.YellowPrimary)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        isError = emailError != null,
                        errorMessage = emailError
                    )

                    RatTravelTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = null },
                        label = stringResource(R.string.login_password),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = if (passwordError != null) RatTravelColors.ErrorRed else RatTravelColors.YellowPrimary)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = stringResource(R.string.login_mostrar_password),
                                    tint = RatTravelColors.OnSurfaceMuted
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); validateAndLogin() }),
                        isError = passwordError != null,
                        errorMessage = passwordError
                    )

                    Button(
                        onClick = { validateAndLogin() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RatTravelColors.YellowPrimary, contentColor = Color(0xFF1A1A1A)),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color(0xFF1A1A1A), strokeWidth = 2.5.dp)
                        } else {
                            Text(stringResource(R.string.login_boton_entrar), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = RatTravelColors.SurfaceVariant)
                Text("  ${stringResource(R.string.login_nuevo_usuario)}  ", style = TextStyle(fontSize = 13.sp, color = RatTravelColors.OnSurfaceMuted))
                HorizontalDivider(modifier = Modifier.weight(1f), color = RatTravelColors.SurfaceVariant)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { navController.navigate("registro") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, RatTravelColors.YellowPrimary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RatTravelColors.YellowPrimary)
            ) {
                Text(stringResource(R.string.login_boton_registro), style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold))
            }

            Spacer(modifier = Modifier.height(32.dp))

            val textoAceptar = stringResource(R.string.login_footer_aceptar)
            val textoTerminos = stringResource(R.string.login_footer_terminos)
            val textoAprender = stringResource(R.string.login_footer_aprender)
            val textoSobre = stringResource(R.string.login_footer_sobre)

            val annotatedFooter = buildAnnotatedString {
                withStyle(SpanStyle(color = RatTravelColors.OnSurfaceMuted, fontSize = 12.sp)) { append(textoAceptar) }
                pushStringAnnotation(tag = "TERMS", annotation = "terminos")
                withStyle(SpanStyle(color = RatTravelColors.YellowAccent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textDecoration = TextDecoration.Underline)) { append(textoTerminos) }
                pop()
                withStyle(SpanStyle(color = RatTravelColors.OnSurfaceMuted, fontSize = 12.sp)) { append(textoAprender) }
                pushStringAnnotation(tag = "ABOUT", annotation = "informacion")
                withStyle(SpanStyle(color = RatTravelColors.YellowAccent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textDecoration = TextDecoration.Underline)) { append(textoSobre) }
                pop()
                withStyle(SpanStyle(color = RatTravelColors.OnSurfaceMuted, fontSize = 12.sp)) { append(".") }
            }

            ClickableText(
                text = annotatedFooter,
                style = TextStyle(textAlign = TextAlign.Center, lineHeight = 20.sp),
                onClick = { offset ->
                    annotatedFooter.getStringAnnotations("TERMS", offset, offset).firstOrNull()?.let { navController.navigate("terminos") }
                    annotatedFooter.getStringAnnotations("ABOUT", offset, offset).firstOrNull()?.let { navController.navigate("about") }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RatTravelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor      = RatTravelColors.YellowPrimary,
                unfocusedBorderColor    = RatTravelColors.SurfaceVariant,
                errorBorderColor        = RatTravelColors.ErrorRed,
                focusedLabelColor       = RatTravelColors.YellowPrimary,
                unfocusedLabelColor     = RatTravelColors.OnSurfaceMuted,
                errorLabelColor         = RatTravelColors.ErrorRed,
                cursorColor             = RatTravelColors.YellowPrimary,
                focusedTextColor        = RatTravelColors.OnSurface,
                unfocusedTextColor      = RatTravelColors.OnSurface,
                focusedContainerColor   = RatTravelColors.SurfaceVariant,
                unfocusedContainerColor = RatTravelColors.SurfaceVariant,
                errorContainerColor     = RatTravelColors.ErrorRed.copy(alpha = 0.08f)
            )
        )
        AnimatedVisibility(visible = isError && errorMessage != null) {
            Text(text = errorMessage ?: "", color = RatTravelColors.ErrorRed, fontSize = 11.sp, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
        }
    }
}