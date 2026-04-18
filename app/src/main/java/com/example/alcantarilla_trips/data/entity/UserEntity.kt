package com.example.alcantarilla_trips.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val userId: String, // El UID que viene de Firebase
    val name: String,
    val email: String,
    val photoUrl: String? = null
)