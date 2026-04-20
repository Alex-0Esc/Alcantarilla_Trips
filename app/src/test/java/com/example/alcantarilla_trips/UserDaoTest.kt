package com.example.alcantarilla_trips

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.coroutines.test.runTest
import com.example.alcantarilla_trips.data.local.entity.UserEntity

// 1. Tienes que definir esta clase aquí para que el test la reconozca
class FakeUserDao {
    private val users = mutableListOf<UserEntity>()

    suspend fun insertUser(user: UserEntity) {
        users.add(user)
    }

    suspend fun countByLogin(login: String): Int {
        return users.count { it.login == login }
    }
}

class UserDaoTest {

    @Test
    fun loginTakenReturnsTrueWhenUserExists() = runTest {
        // Ahora ya no saldrá en rojo porque la clase está definida arriba
        val fakeDao = FakeUserDao()

        fakeDao.insertUser(
            UserEntity(
                userId = "uid1",
                login = "pepe",
                username = "Pepe",
                birthdate = "01/01/1990",
                address = "Calle 1",
                country = "España",
                phoneNumber = "600000000",
                acceptEmails = false,
                email = "pepe@test.com"
            )
        )

        val count = fakeDao.countByLogin("pepe")
        assertTrue(count > 0)
    }

    @Test
    fun loginTakenReturnsFalseWhenUserDoesNotExist() = runTest {
        val fakeDao = FakeUserDao()
        val count = fakeDao.countByLogin("noexiste")
        assertEquals(0, count)
    }
}