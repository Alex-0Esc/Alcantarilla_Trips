package com.example.alcantarilla_trips.domain

data class Trip(
    val tripId: Int,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val destineCity: String,
    val departureCity: String,
    val status: TripStatus = TripStatus.PENDING,
    val flight: String = "",
    val price: Int = 0,
    val hotelName: String = "",
    val imageEmoji: String = "🏙️"
)

enum class TripStatus { PENDING, COMPLETED, CANCELLED }