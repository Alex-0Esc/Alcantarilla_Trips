package com.example.alcantarilla_trips.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "activities",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["tripId"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val activityId: Int = 0,
    val tripId: Int,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val createdAt: Long = System.currentTimeMillis()
)