package com.example.alcantarilla_trips.data.repository

import android.util.Log
import com.example.alcantarilla_trips.data.fakeDB.FakeActivityDataSource
import com.example.alcantarilla_trips.domain.Activity
import com.example.alcantarilla_trips.domain.ActivityRepository

class ActivityRepositoryImpl : ActivityRepository {

    companion object {
        private const val TAG = "ActivityRepository"
    }

    override fun getActivitiesByTrip(tripId: Int): List<Activity> {
        val activities = FakeActivityDataSource.getActivitiesByTrip(tripId)
        Log.d(TAG, "getActivitiesByTrip: ${activities.size} actividades para el viaje $tripId")
        return activities
    }

    override fun getActivityById(activityId: Int): Activity? {
        val activity = FakeActivityDataSource.getActivityById(activityId)
        if (activity == null) Log.e(TAG, "getActivityById: actividad $activityId no encontrada")
        else Log.d(TAG, "getActivityById: encontrada '${activity.title}'")
        return activity
    }

    override fun addActivity(activity: Activity) {
        FakeActivityDataSource.addActivity(activity)
        Log.i(TAG, "addActivity: añadida '${activity.title}' al viaje ${activity.tripId}")
    }

    override fun updateActivity(activity: Activity) {
        FakeActivityDataSource.updateActivity(activity)
        Log.i(TAG, "updateActivity: actualizada '${activity.title}' id ${activity.activityId}")
    }

    override fun deleteActivity(activityId: Int) {
        FakeActivityDataSource.deleteActivity(activityId)
        Log.i(TAG, "deleteActivity: eliminada actividad id $activityId")
    }
}