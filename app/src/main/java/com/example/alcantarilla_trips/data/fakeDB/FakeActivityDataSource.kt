package com.example.alcantarilla_trips.data.fakeDB

import com.example.alcantarilla_trips.domain.Activity
import java.time.LocalDate
import java.time.LocalTime

object FakeActivityDataSource {

    private val activities = mutableListOf(
        Activity(
            activityId = 1,
            tripId = 4,
            title = "Visita a Times Square",
            description = "Paseo por el corazón de Manhattan",
            date = LocalDate.of(2025, 11, 21),
            time = LocalTime.of(10, 0)
        ),
        Activity(
            activityId = 2,
            tripId = 4,
            title = "Central Park",
            description = "Picnic en el parque más famoso del mundo",
            date = LocalDate.of(2025, 11, 22),
            time = LocalTime.of(12, 30)
        ),
        Activity(
            activityId = 3,
            tripId = 4,
            title = "Museo de Arte Moderno",
            description = "Visita al MoMA",
            date = LocalDate.of(2025, 11, 23),
            time = LocalTime.of(9, 0)
        ),
        Activity(
            activityId = 4,
            tripId = 5,
            title = "Torre de Londres",
            description = "Visita histórica a la torre",
            date = LocalDate.of(2026, 1, 15),
            time = LocalTime.of(11, 0)
        )
    )

    fun getActivitiesByTrip(tripId: Int): List<Activity> =
        activities.filter { it.tripId == tripId }

    fun getActivityById(activityId: Int): Activity? =
        activities.find { it.activityId == activityId }

    fun addActivity(activity: Activity) = activities.add(activity)

    fun updateActivity(activity: Activity) {
        val index = activities.indexOfFirst { it.activityId == activity.activityId }
        if (index != -1) activities[index] = activity
    }

    fun deleteActivity(activityId: Int) =
        activities.removeIf { it.activityId == activityId }

    fun nextId(): Int = (activities.maxOfOrNull { it.activityId } ?: 0) + 1
}