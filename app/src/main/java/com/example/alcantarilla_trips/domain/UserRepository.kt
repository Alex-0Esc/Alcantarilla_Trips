package com.example.alcantarilla_trips.domain

import com.example.alcantarilla_trips.data.local.dao.UserDao
import com.example.alcantarilla_trips.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    val userProfile: Flow<UserEntity?> = userDao.getUser()

    suspend fun saveUserProfile(userId: String, name: String, email: String, photo: String?) {
        val entity = UserEntity(userId, name, email, photo)
        userDao.insertUser(entity)
    }

    suspend fun clearProfile() {
        userDao.deleteUser()
    }
}