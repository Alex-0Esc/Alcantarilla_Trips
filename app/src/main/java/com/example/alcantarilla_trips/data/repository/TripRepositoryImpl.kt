package com.example.alcantarilla_trips.data.repository

import com.example.alcantarilla_trips.data.local.dao.TripDao
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao
) : TripRepository {

    override fun getTrips(): Flow<List<Trip>> {
        // Convertimos el Flow de TripEntity a Flow de Trip (Dominio)
        return tripDao.getAllTrips().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTripById(tripId: Int): Trip? {
        return tripDao.getTripById(tripId)?.toDomain()
    }

    override suspend fun addTrip(trip: Trip) {
        tripDao.insertTrip(trip.toEntity())
    }

    override suspend fun editTrip(trip: Trip) {
        tripDao.updateTrip(trip.toEntity())
    }

    override suspend fun deleteTrip(tripId: Int) {
        tripDao.deleteTripById(tripId)
    }
}