package com.example.alcantarilla_trips.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcantarilla_trips.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Observamos el usuario de Room. Si cambia en la BD, la UI se actualiza sola (T1.6/T3)
    val userProfile = userRepository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Función para cerrar sesión y limpiar los datos locales (T3)
    fun logout() {
        viewModelScope.launch {
            userRepository.clearProfile()
        }
    }
}