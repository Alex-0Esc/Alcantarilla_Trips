package com.example.alcantarilla_trips.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trips",
    indices = [Index("userId")]
)
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val tripId: Int = 0,
    val userId: String = "",
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val destineCity: String,
    val departureCity: String,
    val status: String,
    val flight: String,
    val price: Int,
    val hotelName: String,
    val imageEmoji: String,
    val createdAt: Long = System.currentTimeMillis()
)