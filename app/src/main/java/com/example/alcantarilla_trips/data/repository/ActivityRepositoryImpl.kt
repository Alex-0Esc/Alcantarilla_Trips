package com.example.alcantarilla_trips.data.repository

import com.example.alcantarilla_trips.data.local.dao.ActivityDao
import com.example.alcantarilla_trips.domain.Activity
import com.example.alcantarilla_trips.domain.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao
) : ActivityRepository {

    override fun getActivitiesByTrip(tripId: Int): Flow<List<Activity>> {
        return activityDao.getActivitiesByTrip(tripId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getActivityById(activityId: Int): Activity? {
        return activityDao.getActivityById(activityId)?.toDomain()
    }

    override suspend fun addActivity(activity: Activity) {
        activityDao.insertActivity(activity.toEntity())
    }

    override suspend fun updateActivity(activity: Activity) {
        activityDao.updateActivity(activity.toEntity())
    }

    override suspend fun deleteActivity(activityId: Int) {
        activityDao.deleteActivityById(activityId)
    }
}