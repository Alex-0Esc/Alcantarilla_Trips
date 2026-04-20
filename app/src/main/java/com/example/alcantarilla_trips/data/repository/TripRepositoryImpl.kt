package com.example.alcantarilla_trips.data.repository

import android.util.Log
import com.example.alcantarilla_trips.data.local.dao.TripDao
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "TripRepository"

class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao
) : TripRepository {

    // T4.2: Devuelve solo los viajes del usuario logado
    override fun getTripsByUser(userId: String): Flow<List<Trip>> {
        Log.d(TAG, "getTripsByUser: cargando viajes para userId=$userId")
        return tripDao.getTripsByUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTripById(tripId: Int): Trip? {
        return tripDao.getTripById(tripId)?.toDomain()
    }


    override suspend fun editTrip(trip: Trip) {
        Log.d(TAG, "editTrip: actualizando tripId=${trip.tripId}, título='${trip.title}'")
        try {
            tripDao.updateTrip(trip.toEntity())
            Log.i(TAG, "editTrip: tripId=${trip.tripId} actualizado correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "editTrip: error actualizando tripId=${trip.tripId}", e)
            throw e
        }
    }

    override suspend fun addTrip(trip: Trip) {
        Log.d(TAG, "Insertando viaje: '${trip.title}' userId=${trip.userId}")
        try {
            val id = tripDao.insertTrip(trip.toEntity())
            Log.i(TAG, "Viaje insertado con id=$id")
        } catch (e: Exception) {
            Log.e(TAG, "Error insertando viaje '${trip.title}'", e)
            throw e
        }
    }

    override suspend fun deleteTrip(tripId: Int) {
        Log.d(TAG, "Eliminando viaje id=$tripId")
        tripDao.deleteTripById(tripId)
        Log.i(TAG, "Viaje $tripId eliminado")
    }
}