package com.example.alcantarilla_trips.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.alcantarilla_trips.data.local.entity.AccessLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccessLogDao {

    @Insert
    suspend fun insertLog(log: AccessLogEntity)

    @Query("SELECT * FROM access_log ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<AccessLogEntity>>
}