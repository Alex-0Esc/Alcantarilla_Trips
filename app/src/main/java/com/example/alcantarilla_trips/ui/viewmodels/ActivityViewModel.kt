package com.example.alcantarilla_trips.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.alcantarilla_trips.data.fakeDB.FakeActivityDataSource
import com.example.alcantarilla_trips.data.repository.ActivityRepositoryImpl
import com.example.alcantarilla_trips.domain.Activity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime

class ActivityViewModel : ViewModel() {

    companion object {
        private const val TAG = "ActivityViewModel"
    }

    private val repository = ActivityRepositoryImpl()

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities.asStateFlow()

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError.asStateFlow()

    fun loadActivities(tripId: Int) {
        _activities.value = repository.getActivitiesByTrip(tripId)
        Log.d(TAG, "loadActivities: ${_activities.value.size} actividades cargadas para viaje $tripId")
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
            Log.e(TAG, "addActivity: título vacío")
            return
        }
        if (date.isBefore(tripStartDate) || date.isAfter(tripEndDate)) {
            _validationError.value = "La fecha debe estar dentro del rango del viaje"
            Log.e(TAG, "addActivity: fecha fuera del rango del viaje")
            return
        }

        val newActivity = Activity(
            activityId = FakeActivityDataSource.nextId(),
            tripId = tripId,
            title = title,
            description = description,
            date = date,
            time = time
        )

        repository.addActivity(newActivity)
        loadActivities(tripId)
        _validationError.value = null
        Log.i(TAG, "addActivity: actividad '$title' añadida correctamente")
    }

    fun updateActivity(
        activity: Activity,
        tripStartDate: LocalDate,
        tripEndDate: LocalDate
    ) {
        if (activity.title.isBlank()) {
            _validationError.value = "El título no puede estar vacío"
            Log.e(TAG, "updateActivity: título vacío")
            return
        }
        if (activity.date.isBefore(tripStartDate) || activity.date.isAfter(tripEndDate)) {
            _validationError.value = "La fecha debe estar dentro del rango del viaje"
            Log.e(TAG, "updateActivity: fecha fuera del rango del viaje")
            return
        }

        repository.updateActivity(activity)
        loadActivities(activity.tripId)
        _validationError.value = null
        Log.i(TAG, "updateActivity: actividad '${activity.title}' actualizada")
    }

    fun deleteActivity(activityId: Int, tripId: Int) {
        repository.deleteActivity(activityId)
        loadActivities(tripId)
        Log.i(TAG, "deleteActivity: actividad $activityId eliminada")
    }

    fun clearValidationError() {
        _validationError.value = null
    }
}