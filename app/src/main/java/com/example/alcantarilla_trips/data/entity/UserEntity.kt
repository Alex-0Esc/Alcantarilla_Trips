package com.example.alcantarilla_trips.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val login: String,
    val username: String,
    val birthdate: String,
    val address: String,
    val country: String,
    val phoneNumber: String,
    val acceptEmails: Boolean = false,
    val email: String,
    val photoUrl: String? = null
)