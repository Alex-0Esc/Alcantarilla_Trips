package com.example.alcantarilla_trips.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alcantarilla_trips.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTripsByUser(userId: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE tripId = :tripId")
    suspend fun getTripById(tripId: Int): TripEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE tripId = :tripId")
    suspend fun deleteTripById(tripId: Int)
}