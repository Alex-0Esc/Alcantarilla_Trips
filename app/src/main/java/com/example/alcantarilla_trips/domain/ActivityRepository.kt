package com.example.alcantarilla_trips.domain

interface ActivityRepository {
    fun getActivitiesByTrip(tripId: Int): List<Activity>
    fun getActivityById(activityId: Int): Activity?
    fun addActivity(activity: Activity)
    fun updateActivity(activity: Activity)
    fun deleteActivity(activityId: Int)
}