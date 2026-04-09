package com.example.alcantarilla_trips.data.local.dao

import androidx.room.*
import com.example.alcantarilla_trips.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities WHERE tripId = :tripId ORDER BY date ASC, time ASC")
    fun getActivitiesByTrip(tripId: Int): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE activityId = :activityId")
    suspend fun getActivityById(activityId: Int): ActivityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity): Long

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)

    @Query("DELETE FROM activities WHERE activityId = :activityId")
    suspend fun deleteActivityById(activityId: Int)
}