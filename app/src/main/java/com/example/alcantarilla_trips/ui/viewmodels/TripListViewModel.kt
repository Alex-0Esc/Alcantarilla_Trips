package com.example.alcantarilla_trips.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripRepository
import com.example.alcantarilla_trips.domain.TripStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    companion object {
        private const val TAG = "TripListViewModel"
    }

    // Convertimos el Flow del repositorio en un StateFlow para la UI
    // Esto hace que la UI se actualice sola (T1.6)
    val trips: StateFlow<List<Trip>> = repository.getTrips()
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

        viewModelScope.launch {
            val newTrip = Trip(
                tripId = 0, // Room autogenerará el ID real
                title = title,
                description = description,
                startDate = startDate,
                endDate = endDate,
                destineCity = destineCity,
                departureCity = departureCity,
                status = TripStatus.PENDING,
                flight = flight,
                price = price,
                hotelName = hotelName,
                imageEmoji = imageEmoji
            )
            repository.addTrip(newTrip)
            _validationError.value = null
            Log.i(TAG, "addTrip: viaje '$title' guardado en Room")
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