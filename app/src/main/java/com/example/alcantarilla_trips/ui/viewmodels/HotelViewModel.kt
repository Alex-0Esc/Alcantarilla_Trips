package com.example.alcantarilla_trips.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcantarilla_trips.domain.HotelRepository
import com.example.alcantarilla_trips.domain.model.Hotel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HotelViewModel"

sealed class HotelUiState {
    object Idle : HotelUiState()
    object Loading : HotelUiState()
    data class Success(val hotels: List<Hotel>) : HotelUiState()
    data class Error(val message: String) : HotelUiState()
}

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val hotelRepository: HotelRepository,
    private val tripRepository: com.example.alcantarilla_trips.domain.TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HotelUiState>(HotelUiState.Idle)
    val uiState: StateFlow<HotelUiState> = _uiState.asStateFlow()

    /**
     * Busca hoteles para la ciudad y fechas del viaje actual.
     * Las fechas deben llegar en formato "yyyy-MM-dd".
     */
    fun searchHotels(city: String, checkIn: String, checkOut: String) {
        if (city.isBlank()) {
            _uiState.value = HotelUiState.Error("La ciudad de destino no puede estar vacía")
            return
        }
        viewModelScope.launch {
            _uiState.value = HotelUiState.Loading
            Log.d(TAG, "searchHotels: city=$city checkIn=$checkIn checkOut=$checkOut")

            hotelRepository.searchHotels(city, checkIn, checkOut)
                .onSuccess { hotels ->
                    Log.i(TAG, "searchHotels: ${hotels.size} hoteles recibidos")
                    _uiState.value = if (hotels.isEmpty())
                        HotelUiState.Error("No se encontraron hoteles para '$city'")
                    else
                        HotelUiState.Success(hotels)
                }
                .onFailure { error ->
                    Log.e(TAG, "searchHotels: error", error)
                    _uiState.value = HotelUiState.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun resetState() {
        _uiState.value = HotelUiState.Idle
    }

    // T4: All bookings
    val allBookings = hotelRepository.getAllBookings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteBooking(bookingId: Int, tripId: Int = -1) {
        viewModelScope.launch {
            hotelRepository.deleteBooking(bookingId)
            if (tripId > 0) tripRepository.clearHotelName(tripId)
        }
    }

    fun getBookingsByTrip(tripId: Int) = hotelRepository.getBookingsByTrip(tripId)
}