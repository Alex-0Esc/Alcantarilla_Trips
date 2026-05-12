package com.example.alcantarilla_trips.domain.model

// ── Hotel con imágenes (T2.4) ─────────────────────────────────────────────────
data class Hotel(
    val id: String,
    val name: String,
    val starRating: Double,
    val address: String,
    val city: String,
    val country: String,
    val guestRating: String,
    val totalReviews: String,
    val pricePerNight: String,
    val priceExact: Double,
    val thumbnailUrl: String,
    val images: List<String> = emptyList()     // T2.4: todas las imágenes
)

// ── Habitación (T2.2) ─────────────────────────────────────────────────────────
data class Room(
    val roomKey: String,
    val name: String,
    val pricePerNight: String,
    val priceExact: Double,
    val images: List<String> = emptyList(),    // T2.4: imágenes de la habitación
    val includesBreakfast: Boolean = false,
    val freeCancellation: Boolean = false
)

// ── Reserva local (T2.3) ──────────────────────────────────────────────────────
data class Booking(
    val bookingId: Int = 0,
    val tripId: Int = -1,
    val hotelId: String,
    val hotelName: String,
    val roomKey: String,
    val roomName: String,
    val city: String,
    val checkIn: String,
    val checkOut: String,
    val pricePerNight: Double,
    val totalPrice: Double,
    val thumbnailUrl: String
)