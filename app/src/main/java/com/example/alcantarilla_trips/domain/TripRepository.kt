package com.example.alcantarilla_trips.domain

import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTripsByUser(userId: String): Flow<List<Trip>>   // T4.2: filtrado por usuario
    suspend fun getTripById(tripId: Int): Trip?
    suspend fun addTrip(trip: Trip)
    suspend fun editTrip(trip: Trip)
    suspend fun deleteTrip(tripId: Int)
}