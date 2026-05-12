package com.example.alcantarilla_trips.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val bookingId: Int = 0,
    val hotelId: String,
    val hotelName: String,
    val roomKey: String,
    val roomName: String,
    val city: String,
    val checkIn: String,
    val checkOut: String,
    val pricePerNight: Double,
    val totalPrice: Double,
    val thumbnailUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)