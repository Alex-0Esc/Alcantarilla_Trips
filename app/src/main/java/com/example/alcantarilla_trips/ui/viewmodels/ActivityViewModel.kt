package com.example.alcantarilla_trips.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcantarilla_trips.domain.Activity
import com.example.alcantarilla_trips.domain.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    companion object {
        private const val TAG = "ActivityViewModel"
    }

    // Guardamos el ID del viaje actual para reaccionar a él
    private val _currentTripId = MutableStateFlow<Int?>(null)

    // Cada vez que cambie el _currentTripId, se genera un nuevo flujo de la BD
    @OptIn(ExperimentalCoroutinesApi::class)
    val activities: StateFlow<List<Activity>> = _currentTripId
        .filterNotNull()
        .flatMapLatest { tripId ->
            repository.getActivitiesByTrip(tripId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError.asStateFlow()

    /**
     * Establece el viaje actual para cargar sus actividades de forma reactiva.
     */
    fun loadActivities(tripId: Int) {
        _currentTripId.value = tripId
        Log.d(TAG, "loadActivities: observando actividades para el viaje $tripId")
    }

    fun addActivity(
        tripId: Int,
        title: String,
        description: String,
        date: LocalDate,
        time: LocalTime,
        tripStartDate: LocalDate,
        tripEndDate: LocalDate
    ) {
        if (title.isBlank()) {
            _validationError.value = "El título no puede estar vacío"
            return
        }
        if (date.isBefore(tripStartDate) || date.isAfter(tripEndDate)) {
            _validationError.value = "La fecha debe estar dentro del rango del viaje"
            return
        }

        viewModelScope.launch {
            val newActivity = Activity(
                activityId = 0, // Room se encarga de autogenerarlo
                tripId = tripId,
                title = title,
                description = description,
                date = date,
                time = time
            )
            repository.addActivity(newActivity)
            _validationError.value = null
            Log.i(TAG, "addActivity: actividad '$title' guardada en Room")
        }
    }

    fun updateActivity(
        activity: Activity,
        tripStartDate: LocalDate,
        tripEndDate: LocalDate
    ) {
        if (activity.title.isBlank()) {
            _validationError.value = "El título no puede estar vacío"
            return
        }
        if (activity.date.isBefore(tripStartDate) || activity.date.isAfter(tripEndDate)) {
            _validationError.value = "La fecha debe estar dentro del rango del viaje"
            return
        }

        viewModelScope.launch {
            repository.updateActivity(activity)
            _validationError.value = null
            Log.i(TAG, "updateActivity: actividad '${activity.title}' actualizada")
        }
    }

    fun deleteActivity(activityId: Int) {
        viewModelScope.launch {
            repository.deleteActivity(activityId)
            Log.i(TAG, "deleteActivity: actividad $activityId eliminada")
        }
    }

    fun clearValidationError() {
        _validationError.value = null
    }
}