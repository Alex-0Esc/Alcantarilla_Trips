package com.example.alcantarilla_trips.domain

import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivitiesByTrip(tripId: Int): Flow<List<Activity>> // Cambiado a Flow
    suspend fun getActivityById(activityId: Int): Activity?
    suspend fun addActivity(activity: Activity)
    suspend fun updateActivity(activity: Activity)
    suspend fun deleteActivity(activityId: Int)
}