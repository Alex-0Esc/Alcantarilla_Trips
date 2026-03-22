package com.example.alcantarilla_trips.domain

import java.time.LocalDate
import java.time.LocalTime

data class Activity(
    val activityId: Int,
    val tripId: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime
)