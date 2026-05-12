package com.example.alcantarilla_trips.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trip_images",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["tripId"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tripId")]
)
data class TripImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId: Int = 0,
    val tripId: Int,
    val uriString: String,
    val createdAt: Long = System.currentTimeMillis()
)