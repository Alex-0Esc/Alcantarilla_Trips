package com.example.alcantarilla_trips.data.repository

import com.example.alcantarilla_trips.data.local.dao.BookingDao
import com.example.alcantarilla_trips.data.local.entity.BookingEntity
import com.example.alcantarilla_trips.data.remote.HotelApiService
import com.example.alcantarilla_trips.data.remote.model.toDomain
import com.example.alcantarilla_trips.domain.HotelRepository
import com.example.alcantarilla_trips.domain.model.Booking
import com.example.alcantarilla_trips.domain.model.Hotel
import com.example.alcantarilla_trips.domain.model.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val apiService: HotelApiService,
    private val bookingDao: BookingDao
) : HotelRepository {

    // T2.1: Buscar hoteles (paso 1: ciudad → destinationId, paso 2: hoteles)
    override suspend fun searchHotels(
        city: String,
        checkIn: String,
        checkOut: String
    ): Result<List<Hotel>> {
        return try {
            val locationResponse = apiService.searchLocation(query = city)
            if (!locationResponse.isSuccessful)
                return Result.failure(Exception("Error ubicación: ${locationResponse.code()}"))

            val destinationId = locationResponse.body()
                ?.suggestions
                ?.firstOrNull { it.group == "CITY_GROUP" || it.group == "HOTEL" }
                ?.entities?.firstOrNull()?.destinationId
                ?: return Result.failure(Exception("No se encontró la ciudad '$city'"))

            val hotelsResponse = apiService.searchHotels(
                destinationId = destinationId,
                checkIn = checkIn,
                checkOut = checkOut
            )
            if (!hotelsResponse.isSuccessful)
                return Result.failure(Exception("Error hoteles: ${hotelsResponse.code()}"))

            val hotels = hotelsResponse.body()
                ?.data?.body?.searchResults?.results
                ?.map { it.toDomain() } ?: emptyList()

            Result.success(hotels)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // T2.2: Obtener detalle del hotel con habitaciones e imágenes
    override suspend fun getHotelDetail(
        hotelId: String,
        checkIn: String,
        checkOut: String
    ): Result<Pair<Hotel, List<Room>>> {
        return try {
            val response = apiService.getHotelDetail(
                hotelId  = hotelId,
                checkIn  = checkIn,
                checkOut = checkOut
            )
            if (!response.isSuccessful)
                return Result.failure(Exception("Error detalle: ${response.code()}"))

            val body = response.body()?.data?.body
            val propDesc = body?.propertyDescription

            val hotel = Hotel(
                id            = hotelId,
                name          = propDesc?.name ?: "",
                starRating    = propDesc?.starRating ?: 0.0,
                address       = propDesc?.address?.streetAddress ?: "",
                city          = propDesc?.address?.locality ?: "",
                country       = propDesc?.address?.countryName ?: "",
                guestRating   = "N/A",
                totalReviews  = "0",
                pricePerNight = "N/A",
                priceExact    = 0.0,
                thumbnailUrl  = propDesc?.featuredImageUrl?.url ?: "",
                images        = propDesc?.images?.mapNotNull { it.baseUrl } ?: emptyList()
            )

            val rooms = body?.roomsAndRates?.rooms
                ?.map { it.toDomain() } ?: emptyList()

            Result.success(Pair(hotel, rooms))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // T2.3: Guardar reserva en Room local
    override suspend fun bookRoom(booking: Booking): Result<Int> {
        return try {
            val entity = BookingEntity(
                hotelId      = booking.hotelId,
                hotelName    = booking.hotelName,
                roomKey      = booking.roomKey,
                roomName     = booking.roomName,
                city         = booking.city,
                checkIn      = booking.checkIn,
                checkOut     = booking.checkOut,
                pricePerNight = booking.pricePerNight,
                totalPrice   = booking.totalPrice,
                thumbnailUrl = booking.thumbnailUrl
            )
            val id = bookingDao.insertBooking(entity).toInt()
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // T2.3: Listar todas las reservas
    override fun getAllBookings(): Flow<List<Booking>> =
        bookingDao.getAllBookings().map { list -> list.map { it.toDomain() } }

    // T4: Eliminar reserva
    override suspend fun deleteBooking(bookingId: Int) {
        bookingDao.deleteBooking(bookingId)
    }

    // T4: Reservas de un viaje
    override fun getBookingsByTrip(tripId: Int): Flow<List<Booking>> =
        bookingDao.getBookingsByTrip(tripId).map { list -> list.map { it.toDomain() } }
}

private fun com.example.alcantarilla_trips.data.local.entity.BookingEntity.toDomain() = Booking(
    bookingId    = bookingId,
    tripId       = tripId,
    hotelId      = hotelId,
    hotelName    = hotelName,
    roomKey      = roomKey,
    roomName     = roomName,
    city         = city,
    checkIn      = checkIn,
    checkOut     = checkOut,
    pricePerNight = pricePerNight,
    totalPrice   = totalPrice,
    thumbnailUrl = thumbnailUrl
)