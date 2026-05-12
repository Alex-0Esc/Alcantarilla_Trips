package com.example.alcantarilla_trips.domain

import kotlinx.coroutines.flow.Flow

interface TripImageRepository {
    fun getImagesByTrip(tripId: Int): Flow<List<TripImage>>
    suspend fun addImage(tripId: Int, uriString: String)
    suspend fun deleteImage(imageId: Int)
}

data class TripImage(
    val imageId: Int,
    val tripId: Int,
    val uriString: String,
    val createdAt: Long
)