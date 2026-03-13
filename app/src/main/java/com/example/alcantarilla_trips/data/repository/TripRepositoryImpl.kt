package com.example.alcantarilla_trips.data.repository

import android.util.Log
import com.example.alcantarilla_trips.data.fakeDB.FakeTripDataSource
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripRepository

class TripRepositoryImpl : TripRepository {

    companion object {
        private const val TAG = "TripRepository"
    }

    override fun getTrips(): List<Trip> {
        Log.d(TAG, "getTrips: recuperando ${FakeTripDataSource.getTrips().size} viajes")
        return FakeTripDataSource.getTrips()
    }

    override fun getTripById(tripId: Int): Trip? {
        val trip = FakeTripDataSource.getTripById(tripId)
        if (trip == null) Log.e(TAG, "getTripById: viaje $tripId no encontrado")
        else Log.d(TAG, "getTripById: encontrado ${trip.title}")
        return trip
    }

    override fun addTrip(trip: Trip) {
        FakeTripDataSource.addTrip(trip)
        Log.i(TAG, "addTrip: añadido '${trip.title}' con id ${trip.tripId}")
    }

    override fun editTrip(trip: Trip) {
        FakeTripDataSource.editTrip(trip)
        Log.i(TAG, "editTrip: actualizado '${trip.title}' id ${trip.tripId}")
    }

    override fun deleteTrip(tripId: Int) {
        FakeTripDataSource.deleteTrip(tripId)
        Log.i(TAG, "deleteTrip: eliminado viaje id $tripId")
    }
}