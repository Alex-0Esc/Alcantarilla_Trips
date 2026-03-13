package com.example.alcantarilla_trips.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.alcantarilla_trips.data.fakeDB.FakeTripDataSource
import com.example.alcantarilla_trips.data.repository.TripRepositoryImpl
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TripListViewModel : ViewModel() {

    companion object {
        private const val TAG = "TripListViewModel"
    }

    private val repository = TripRepositoryImpl()

    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError.asStateFlow()

    init {
        loadTrips()
    }

    fun loadTrips() {
        _trips.value = repository.getTrips()
        Log.d(TAG, "loadTrips: ${_trips.value.size} viajes cargados")
    }

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
            Log.e(TAG, "addTrip: título vacío")
            return
        }
        // Validar fechas solo si están rellenas (hasta implementar DatePicker)
        if (startDate.isNotBlank() && endDate.isNotBlank() && !isStartBeforeEnd(startDate, endDate)) {
            _validationError.value = "La fecha de inicio debe ser anterior a la fecha de fin"
            Log.e(TAG, "addTrip: startDate no es anterior a endDate")
            return
        }

        val newTrip = Trip(
            tripId = FakeTripDataSource.nextId(),
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
            destineCity = destineCity,
            departureCity = departureCity,
            flight = flight,
            price = price,
            hotelName = hotelName,
            status = TripStatus.PENDING,
            imageEmoji = imageEmoji
        )

        repository.addTrip(newTrip)
        loadTrips()
        _validationError.value = null
        Log.i(TAG, "addTrip: viaje '$title' añadido correctamente")
    }

    fun editTrip(trip: Trip) {
        if (trip.title.isBlank()) {
            _validationError.value = "El título no puede estar vacío"
            Log.e(TAG, "editTrip: título vacío")
            return
        }
        if (trip.startDate.isNotBlank() && trip.endDate.isNotBlank() && !isStartBeforeEnd(trip.startDate, trip.endDate)) {
            _validationError.value = "La fecha de inicio debe ser anterior a la fecha de fin"
            Log.e(TAG, "editTrip: fechas inválidas")
            return
        }
        repository.editTrip(trip)
        loadTrips()
        _validationError.value = null
        Log.i(TAG, "editTrip: viaje '${trip.title}' actualizado")
    }

    fun deleteTrip(tripId: Int) {
        repository.deleteTrip(tripId)
        loadTrips()
        Log.i(TAG, "deleteTrip: viaje $tripId eliminado")
    }

    fun clearValidationError() {
        _validationError.value = null
    }

    private fun isStartBeforeEnd(startDate: String, endDate: String): Boolean {
        return try {
            val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val start = java.time.LocalDate.parse(startDate, formatter)
            val end = java.time.LocalDate.parse(endDate, formatter)
            start.isBefore(end)
        } catch (e: Exception) {
            Log.e(TAG, "isStartBeforeEnd: error parseando fechas - ${e.message}")
            false
        }
    }
}