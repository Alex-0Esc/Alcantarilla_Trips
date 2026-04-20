package com.example.alcantarilla_trips.data.repository

import com.example.alcantarilla_trips.data.local.entity.ActivityEntity
import com.example.alcantarilla_trips.data.local.entity.TripEntity
import com.example.alcantarilla_trips.domain.Activity
import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripStatus
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// --- Trip Mappers ---

fun TripEntity.toDomain(): Trip = Trip(
    tripId        = tripId,
    userId        = userId,       // T4.2
    title         = title,
    description   = description,
    startDate     = startDate,
    endDate       = endDate,
    destineCity   = destineCity,
    departureCity = departureCity,
    status        = try { TripStatus.valueOf(status) } catch (e: Exception) { TripStatus.PENDING },
    flight        = flight,
    price         = price,
    hotelName     = hotelName,
    imageEmoji    = imageEmoji
)

fun Trip.toEntity(): TripEntity = TripEntity(
    tripId        = tripId,
    userId        = userId,       // T4.2
    title         = title,
    description   = description,
    startDate     = startDate,
    endDate       = endDate,
    destineCity   = destineCity,
    departureCity = departureCity,
    status        = status.name,
    flight        = flight,
    price         = price,
    hotelName     = hotelName,
    imageEmoji    = imageEmoji
)

// --- Activity Mappers ---

private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

fun ActivityEntity.toDomain(): Activity = Activity(
    activityId  = activityId,
    tripId      = tripId,
    title       = title,
    description = description,
    date        = LocalDate.parse(date, dateFormatter),
    time        = LocalTime.parse(time, timeFormatter)
)

fun Activity.toEntity(): ActivityEntity = ActivityEntity(
    activityId  = activityId,
    tripId      = tripId,
    title       = title,
    description = description,
    date        = date.format(dateFormatter),
    time        = time.format(timeFormatter)
)