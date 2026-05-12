package com.example.alcantarilla_trips.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alcantarilla_trips.data.local.entity.TripImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: TripImageEntity): Long

    @Query("SELECT * FROM trip_images WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getImagesByTrip(tripId: Int): Flow<List<TripImageEntity>>

    @Query("DELETE FROM trip_images WHERE imageId = :imageId")
    suspend fun deleteImage(imageId: Int)
}