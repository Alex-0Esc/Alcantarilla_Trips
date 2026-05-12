package com.example.alcantarilla_trips.domain

import com.example.alcantarilla_trips.domain.model.Booking
import com.example.alcantarilla_trips.domain.model.Hotel
import com.example.alcantarilla_trips.domain.model.Room
import kotlinx.coroutines.flow.Flow

interface HotelRepository {
    // T2.1: Buscar hoteles por ciudad y fechas
    suspend fun searchHotels(city: String, checkIn: String, checkOut: String): Result<List<Hotel>>

    // T2.2: Obtener detalle del hotel con habitaciones
    suspend fun getHotelDetail(hotelId: String, checkIn: String, checkOut: String): Result<Pair<Hotel, List<Room>>>

    // T2.3: Guardar reserva localmente
    suspend fun bookRoom(booking: Booking): Result<Int>

    // T2.3: Obtener todas las reservas
    fun getAllBookings(): Flow<List<Booking>>
}