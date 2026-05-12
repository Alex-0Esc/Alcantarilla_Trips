package com.example.alcantarilla_trips.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alcantarilla_trips.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE bookingId = :id")
    suspend fun getBookingById(id: Int): BookingEntity?

    @Query("DELETE FROM bookings WHERE bookingId = :id")
    suspend fun deleteBooking(id: Int)

    @Query("SELECT * FROM bookings WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getBookingsByTrip(tripId: Int): Flow<List<BookingEntity>>
}