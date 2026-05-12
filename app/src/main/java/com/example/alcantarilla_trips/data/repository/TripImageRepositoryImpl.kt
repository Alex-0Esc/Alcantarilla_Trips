package com.example.alcantarilla_trips.data.repository

import com.example.alcantarilla_trips.data.local.dao.TripImageDao
import com.example.alcantarilla_trips.data.local.entity.TripImageEntity
import com.example.alcantarilla_trips.domain.TripImage
import com.example.alcantarilla_trips.domain.TripImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TripImageRepositoryImpl @Inject constructor(
    private val dao: TripImageDao
) : TripImageRepository {

    override fun getImagesByTrip(tripId: Int): Flow<List<TripImage>> =
        dao.getImagesByTrip(tripId).map { list ->
            list.map { TripImage(it.imageId, it.tripId, it.uriString, it.createdAt) }
        }

    override suspend fun addImage(tripId: Int, uriString: String) {
        dao.insertImage(TripImageEntity(tripId = tripId, uriString = uriString))
    }

    override suspend fun deleteImage(imageId: Int) {
        dao.deleteImage(imageId)
    }
}