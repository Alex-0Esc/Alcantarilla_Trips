package com.example.alcantarilla_trips.data.remote.model

import com.example.alcantarilla_trips.domain.model.Hotel
import com.example.alcantarilla_trips.domain.model.Room

fun HotelDto.toDomain(): Hotel = Hotel(
    id           = id,
    name         = name,
    starRating   = starRating,
    address      = address?.streetAddress ?: "",
    city         = address?.locality ?: "",
    country      = address?.countryName ?: "",
    guestRating  = guestReviews?.rating ?: "N/A",
    totalReviews = guestReviews?.total ?: "0",
    pricePerNight = ratePlan?.price?.current ?: "N/A",
    priceExact   = ratePlan?.price?.exactCurrent ?: 0.0,
    thumbnailUrl = thumbUrls?.srpDesktop ?: "",
    images       = urls?.hotelImages?.mapNotNull { it.baseUrl } ?: emptyList()
)

fun RoomDto.toDomain(): Room = Room(
    roomKey            = roomKey ?: "",
    name               = name ?: "Habitación",
    pricePerNight      = ratePlans?.firstOrNull()?.price?.current ?: "N/A",
    priceExact         = ratePlans?.firstOrNull()?.price?.exactCurrent ?: 0.0,
    images             = images?.mapNotNull { it.baseUrl } ?: emptyList(),
    includesBreakfast  = ratePlans?.firstOrNull()?.features?.breakfast ?: false,
    freeCancellation   = ratePlans?.firstOrNull()?.features?.freeCancellation ?: false
)