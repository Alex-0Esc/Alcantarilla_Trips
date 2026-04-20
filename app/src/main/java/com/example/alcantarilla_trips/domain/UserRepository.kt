package com.example.alcantarilla_trips.domain

import android.util.Log
import com.example.alcantarilla_trips.data.local.dao.UserDao
import com.example.alcantarilla_trips.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "UserRepository"

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    val userProfile: Flow<UserEntity?> = userDao.getUser()

    // T4.1: Comprobar si el username ya está en uso
    suspend fun isLoginTaken(login: String): Boolean {
        val count = userDao.countByLogin(login)
        Log.d(TAG, "isLoginTaken: login='$login' count=$count")
        return count > 0
    }

    // T4.1: Guardar perfil completo con todos los campos obligatorios
    suspend fun saveUserProfile(
        userId: String,
        login: String,
        username: String,
        birthdate: String,
        address: String,
        country: String,
        phoneNumber: String,
        acceptEmails: Boolean,
        email: String,
        photo: String?
    ) {
        Log.d(TAG, "saveUserProfile: userId=$userId, login=$login")
        val entity = UserEntity(
            userId       = userId,
            login        = login,
            username     = username,
            birthdate    = birthdate,
            address      = address,
            country      = country,
            phoneNumber  = phoneNumber,
            acceptEmails = acceptEmails,
            email        = email,
            photoUrl     = photo
        )
        userDao.insertUser(entity)
        Log.i(TAG, "saveUserProfile: perfil guardado para userId=$userId")
    }

    suspend fun clearProfile() {
        Log.i(TAG, "clearProfile: limpiando perfil de Room (logout)")
        userDao.deleteUser()
    }
}