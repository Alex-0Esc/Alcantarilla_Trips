package com.example.alcantarilla_trips.data.fakeDB

import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripStatus

object FakeTripDataSource {

    private val trips = mutableListOf(
        Trip(tripId = 1, title = "Escapada a París", description = "Ciudad del amor", startDate = "12/03/2025", endDate = "16/03/2025", destineCity = "París", departureCity = "Madrid", flight = "IB3456", price = 248, hotelName = "Hotel Roquefort Palace", status = TripStatus.COMPLETED, imageEmoji = "🗼"),
        Trip(tripId = 2, title = "Tokio Express", description = "Aventura japonesa", startDate = "28/06/2025", endDate = "05/07/2025", destineCity = "Tokio", departureCity = "Barcelona", flight = "JL7712", price = 680, hotelName = "Madriguera Boutique", status = TripStatus.COMPLETED, imageEmoji = "🗾"),
        Trip(tripId = 3, title = "Roma Eterna", description = "Historia italiana", startDate = "05/08/2025", endDate = "10/08/2025", destineCity = "Roma", departureCity = "Madrid", flight = "VY1234", price = 193, hotelName = "Cloaca Suites", status = TripStatus.COMPLETED, imageEmoji = "🏛️"),
        Trip(tripId = 4, title = "Nueva York en otoño", description = "La gran manzana", startDate = "20/11/2025", endDate = "27/11/2025", destineCity = "Nueva York", departureCity = "Valencia", flight = "IB0091", price = 590, hotelName = "Alcantarilla Inn", status = TripStatus.PENDING, imageEmoji = "🗽"),
        Trip(tripId = 5, title = "Londres de invierno", description = "Mercados navideños", startDate = "14/01/2026", endDate = "18/01/2026", destineCity = "Londres", departureCity = "Sevilla", flight = "BA2341", price = 312, hotelName = "Madriguera Boutique", status = TripStatus.PENDING, imageEmoji = "🎡"),
        Trip(tripId = 6, title = "Ámsterdam en primavera", description = "Tulipanes y canales", startDate = "03/03/2026", endDate = "07/03/2026", destineCity = "Ámsterdam", departureCity = "Madrid", flight = "VY5566", price = 275, hotelName = "Canal Rat Hotel", status = TripStatus.PENDING, imageEmoji = "🌷"),
        Trip(tripId = 7, title = "Lisboa escapada", description = "Fados y pasteles", startDate = "19/04/2026", endDate = "22/04/2026", destineCity = "Lisboa", departureCity = "Barcelona", flight = "TP8823", price = 130, hotelName = "Tejo Suites", status = TripStatus.CANCELLED, imageEmoji = "🇵🇹")
    )

    fun getTrips(): List<Trip> = trips.toList()

    fun getTripById(tripId: Int): Trip? = trips.find { it.tripId == tripId }

    fun addTrip(trip: Trip) = trips.add(trip)

    fun editTrip(trip: Trip) {
        val index = trips.indexOfFirst { it.tripId == trip.tripId }
        if (index != -1) trips[index] = trip
    }

    fun deleteTrip(tripId: Int) = trips.removeIf { it.tripId == tripId }

    fun nextId(): Int = (trips.maxOfOrNull { it.tripId } ?: 0) + 1
}