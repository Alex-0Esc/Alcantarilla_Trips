package com.example.alcantarilla_trips.domain

import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTripsByUser(userId: String): Flow<List<Trip>>
    suspend fun getTripById(tripId: Int): Trip?
    suspend fun addTrip(trip: Trip)
    suspend fun addTripReturningId(trip: Trip): Int
    suspend fun editTrip(trip: Trip)
    suspend fun deleteTrip(tripId: Int)
    suspend fun clearHotelName(tripId: Int)

}