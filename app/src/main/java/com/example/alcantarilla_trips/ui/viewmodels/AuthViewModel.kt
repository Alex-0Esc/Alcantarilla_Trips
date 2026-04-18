package com.example.alcantarilla_trips.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcantarilla_trips.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "AuthViewModel"

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
    object LoggedOut : AuthState()
    object EmailVerificationSent : AuthState()   // T3.2: email de verificación enviado
    object PasswordResetSent : AuthState()       // T3.3: email de recuperación enviado
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    val currentUser: FirebaseUser? get() = auth.currentUser

    fun checkAuthState() {
        val user = auth.currentUser
        if (user != null) {
            Log.i(TAG, "Sesión activa — UID: ${user.uid}, email: ${user.email}")
            _authState.value = AuthState.Success(user)
        } else {
            Log.i(TAG, "No hay sesión activa")
            _authState.value = AuthState.LoggedOut
        }
    }

    // T2.3: Login — bloquea si el email no está verificado
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Log.w(TAG, "Login fallido — campos vacíos")
            _authState.value = AuthState.Error("Rellena todos los campos")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            Log.d(TAG, "Intentando login con: $email")
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user!!

                // T3.2: Verificamos que el email esté confirmado
                user.reload().await()
                if (!user.isEmailVerified) {
                    Log.w(TAG, "Email no verificado — UID: ${user.uid}")
                    auth.signOut()
                    _authState.value = AuthState.Error(
                        "Debes verificar tu correo antes de entrar.\nRevisa tu bandeja de entrada."
                    )
                    return@launch
                }

                Log.i(TAG, "Login exitoso — UID: ${user.uid}")
                userRepository.saveUserProfile(
                    userId = user.uid,
                    name  = user.displayName ?: "Viajero",
                    email = user.email ?: "",
                    photo = user.photoUrl?.toString()
                )
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                Log.e(TAG, "Login fallido", e)
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // T3.2: Registro con patrón Repository + envío de verificación de email
    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Log.w(TAG, "Registro fallido — campos vacíos")
            _authState.value = AuthState.Error("Rellena todos los campos")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            Log.d(TAG, "Intentando registro con: $email")
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user!!
                Log.i(TAG, "Usuario creado en Firebase — UID: ${user.uid}")

                // T3.2: Enviamos verificación de email
                user.sendEmailVerification().await()
                Log.i(TAG, "Email de verificación enviado a: ${user.email}")

                // Guardamos perfil en Room (T3 repository pattern)
                userRepository.saveUserProfile(
                    userId = user.uid,
                    name  = user.displayName ?: "Viajero",
                    email = user.email ?: "",
                    photo = null
                )

                // Cerramos sesión hasta que verifique el email
                auth.signOut()
                Log.i(TAG, "Sesión cerrada hasta verificación de email")

                _authState.value = AuthState.EmailVerificationSent
            } catch (e: Exception) {
                Log.e(TAG, "Registro fallido", e)
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // T3.3: Recuperación de contraseña
    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Introduce tu correo electrónico")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            Log.d(TAG, "Enviando recuperación a: $email")
            try {
                auth.sendPasswordResetEmail(email).await()
                Log.i(TAG, "Email de recuperación enviado a: $email")
                _authState.value = AuthState.PasswordResetSent
            } catch (e: Exception) {
                Log.e(TAG, "Error enviando recuperación", e)
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // T2.4: Logout
    fun logout() {
        val uid = auth.currentUser?.uid
        auth.signOut()
        Log.i(TAG, "Logout — UID: $uid")
        viewModelScope.launch {
            userRepository.clearProfile()
        }
        _authState.value = AuthState.LoggedOut
    }

    fun clearError() {
        _authState.value = AuthState.Idle
    }
}