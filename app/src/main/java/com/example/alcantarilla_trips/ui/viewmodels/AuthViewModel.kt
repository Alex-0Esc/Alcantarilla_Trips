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
import com.example.alcantarilla_trips.data.local.dao.AccessLogDao
import com.example.alcantarilla_trips.data.local.entity.AccessLogEntity

private const val TAG = "AuthViewModel"

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
    object LoggedOut : AuthState()
    object EmailVerificationSent : AuthState()   // T3.2
    object PasswordResetSent : AuthState()       // T3.3
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val accessLogDao: AccessLogDao       // T4.4
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

    // T2.3 + T4.1: Login — bloquea si el email no está verificado
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

                // T4.4: Registrar acceso en la tabla access_log
                accessLogDao.insertLog(
                    AccessLogEntity(userId = user.uid, action = "LOGIN")
                )
                Log.d(TAG, "Acceso LOGIN registrado para UID: ${user.uid}")

                userRepository.saveUserProfile(
                    userId      = user.uid,
                    login       = user.email ?: "",
                    username    = user.displayName ?: "Viajero",
                    birthdate   = "",
                    address     = "",
                    country     = "",
                    phoneNumber = "",
                    acceptEmails = false,
                    email       = user.email ?: "",
                    photo       = user.photoUrl?.toString()
                )
                _authState.value = AuthState.Success(user)

            } catch (e: Exception) {
                Log.e(TAG, "Login fallido", e)
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // T3.2 + T4.1: Registro con todos los campos del usuario
    fun register(
        email: String,
        password: String,
        login: String,
        birthdate: String,
        address: String,
        country: String,
        phoneNumber: String,
        acceptEmails: Boolean
    ) {
        if (email.isBlank() || password.isBlank()) {
            Log.w(TAG, "Registro fallido — campos vacíos")
            _authState.value = AuthState.Error("Rellena todos los campos")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            Log.d(TAG, "Intentando registro con: $email")

            // T4.1: Comprobar si el username ya está en uso ANTES de crear en Firebase
            val loginTaken = userRepository.isLoginTaken(login)
            if (loginTaken) {
                Log.w(TAG, "Registro fallido — login '$login' ya en uso")
                _authState.value = AuthState.Error("El nombre de usuario ya está en uso")
                return@launch
            }

            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user!!
                Log.i(TAG, "Usuario creado en Firebase — UID: ${user.uid}")

                // T3.2: Enviamos verificación de email
                user.sendEmailVerification().await()
                Log.i(TAG, "Email de verificación enviado a: ${user.email}")

                // T4.1: Guardamos perfil completo en Room
                userRepository.saveUserProfile(
                    userId       = user.uid,
                    login        = login,
                    username     = login,
                    birthdate    = birthdate,
                    address      = address,
                    country      = country,
                    phoneNumber  = phoneNumber,
                    acceptEmails = acceptEmails,
                    email        = email,
                    photo        = null
                )
                Log.i(TAG, "Perfil guardado en Room para UID: ${user.uid}")

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

    // T2.4 + T4.4: Logout — registra el acceso en access_log
    fun logout() {
        val uid = auth.currentUser?.uid
        viewModelScope.launch {
            if (uid != null) {
                // T4.4: Registrar logout en la tabla access_log
                accessLogDao.insertLog(
                    AccessLogEntity(userId = uid, action = "LOGOUT")
                )
                Log.d(TAG, "Acceso LOGOUT registrado para UID: $uid")
            }
            userRepository.clearProfile()
        }
        auth.signOut()
        Log.i(TAG, "Logout — UID: $uid")
        _authState.value = AuthState.LoggedOut
    }

    fun clearError() {
        _authState.value = AuthState.Idle
    }
}