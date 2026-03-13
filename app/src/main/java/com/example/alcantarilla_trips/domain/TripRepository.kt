package com.example.alcantarilla_trips.domain

interface TripRepository {
    fun getTrips(): List<Trip>
    fun getTripById(tripId: Int): Trip?
    fun addTrip(trip: Trip)
    fun editTrip(trip: Trip)
    fun deleteTrip(tripId: Int)
}