package com.example.alcantarilla_trips.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripRepository
import com.example.alcantarilla_trips.domain.TripStatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(
    private val repository: TripRepository,
    private val auth: FirebaseAuth           // T4.2: necesitamos el UID del usuario
) : ViewModel() {

    companion object {
        private const val TAG = "TripListViewModel"
    }

    // T4.2: Obtener solo los viajes del usuario logado.
    // flatMapLatest garantiza que si el usuario cambia (logout/login) el flow se actualiza.
    @OptIn(ExperimentalCoroutinesApi::class)
    val trips: StateFlow<List<Trip>> = flow {
        emit(auth.currentUser?.uid ?: "")
    }
        .flatMapLatest { uid ->
            Log.d(TAG, "Cargando viajes para UID: $uid")
            repository.getTripsByUser(uid)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError.asStateFlow()

    fun addTrip(
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        destineCity: String,
        departureCity: String,
        flight: String = "",
        price: Int = 0,
        hotelName: String = "",
        imageEmoji: String = "🏙️"
    ) {
        if (title.isBlank()) {
            _validationError.value = "El título no puede estar vacío"
            return
        }

        // T5.2: Prevenir nombres de viaje duplicados
        val currentTrips = trips.value
        if (currentTrips.any { it.title.equals(title, ignoreCase = true) }) {
            _validationError.value = "Ya existe un viaje con ese nombre"
            return
        }

        val uid = auth.currentUser?.uid ?: ""

        viewModelScope.launch {
            val newTrip = Trip(
                tripId        = 0,       // Room autogenerará el ID
                userId        = uid,     // T4.2: asociar al usuario logado
                title         = title,
                description   = description,
                startDate     = startDate,
                endDate       = endDate,
                destineCity   = destineCity,
                departureCity = departureCity,
                status        = TripStatus.PENDING,
                flight        = flight,
                price         = price,
                hotelName     = hotelName,
                imageEmoji    = imageEmoji
            )
            repository.addTrip(newTrip)
            _validationError.value = null
            Log.i(TAG, "addTrip: viaje '$title' guardado para UID: $uid")
        }
    }

    fun editTrip(trip: Trip) {
        if (trip.title.isBlank()) {
            _validationError.value = "El título no puede estar vacío"
            return
        }
        viewModelScope.launch {
            repository.editTrip(trip)
            _validationError.value = null
            Log.i(TAG, "editTrip: viaje '${trip.title}' actualizado")
        }
    }

    fun deleteTrip(tripId: Int) {
        viewModelScope.launch {
            repository.deleteTrip(tripId)
            Log.i(TAG, "deleteTrip: viaje $tripId eliminado")
        }
    }

    fun clearValidationError() {
        _validationError.value = null
    }
}