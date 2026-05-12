package com.example.alcantarilla_trips.data.remote

import com.example.alcantarilla_trips.data.remote.model.HotelDetailResponse
import com.example.alcantarilla_trips.data.remote.model.HotelLocationResponse
import com.example.alcantarilla_trips.data.remote.model.HotelSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HotelApiService {

    // T2.1: Buscar destinationId para una ciudad
    @GET("locations/search")
    suspend fun searchLocation(
        @Query("query")  query: String,
        @Query("locale") locale: String = "es_ES"
    ): Response<HotelLocationResponse>

    // T2.1: Buscar hoteles disponibles
    @GET("properties/list")
    suspend fun searchHotels(
        @Query("destinationId") destinationId: String,
        @Query("pageNumber")    pageNumber: Int   = 1,
        @Query("pageSize")      pageSize: Int     = 10,
        @Query("checkIn")       checkIn: String,
        @Query("checkOut")      checkOut: String,
        @Query("adults1")       adults: Int       = 1,
        @Query("sortOrder")     sortOrder: String = "PRICE",
        @Query("locale")        locale: String    = "es_ES",
        @Query("currency")      currency: String  = "EUR"
    ): Response<HotelSearchResponse>

    // T2.2: Detalle del hotel con habitaciones e imágenes
    @GET("properties/get-details")
    suspend fun getHotelDetail(
        @Query("id")       hotelId: String,
        @Query("checkIn")  checkIn: String,
        @Query("checkOut") checkOut: String,
        @Query("adults1")  adults: Int    = 1,
        @Query("locale")   locale: String = "es_ES",
        @Query("currency") currency: String = "EUR"
    ): Response<HotelDetailResponse>
}