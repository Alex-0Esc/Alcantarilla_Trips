// ui/viewmodels/AuthViewModel.kt
package com.example.alcantarilla_trips.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Usuario actualmente logueado (null si no hay sesión)
    val currentUser: FirebaseUser? get() = auth.currentUser

    // T2: Comprueba si hay sesión activa al iniciar la app
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

    // T2.3: Login con email y contraseña
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
                Log.i(TAG, "Login exitoso — UID: ${user.uid}")
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                Log.e(TAG, "Login fallido", e)
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Registro con email y contraseña
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
                Log.i(TAG, "Registro exitoso — UID: ${user.uid}")
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                Log.e(TAG, "Registro fallido", e)
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // T2.4: Logout
    fun logout() {
        val uid = auth.currentUser?.uid
        auth.signOut()
        Log.i(TAG, "Logout — UID: $uid")
        _authState.value = AuthState.LoggedOut
    }

    // Limpia errores para no mostrarlos al volver a la pantalla
    fun clearError() {
        _authState.value = AuthState.Idle
    }
}