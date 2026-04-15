package com.example.alcantarilla_trips.domain

import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTrips(): Flow<List<Trip>> // Cambiado a Flow
    suspend fun getTripById(tripId: Int): Trip?
    suspend fun addTrip(trip: Trip)
    suspend fun editTrip(trip: Trip)
    suspend fun deleteTrip(tripId: Int)
}