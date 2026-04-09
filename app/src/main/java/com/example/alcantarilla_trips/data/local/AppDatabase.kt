package com.example.alcantarilla_trips.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.alcantarilla_trips.data.local.dao.ActivityDao
import com.example.alcantarilla_trips.data.local.dao.TripDao
import com.example.alcantarilla_trips.data.local.entity.ActivityEntity
import com.example.alcantarilla_trips.data.local.entity.TripEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TripEntity::class, ActivityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ActivityDao

    companion object {
        const val DATABASE_NAME = "alcantarilla_trips_db"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Poblar con datos de muestra al crear la BD
                        CoroutineScope(Dispatchers.IO).launch {
                            buildDatabase(context).also { database ->
                                populateDatabase(database.tripDao(), database.activityDao())
                            }
                        }
                    }
                })
                .build()
        }

        private suspend fun populateDatabase(tripDao: TripDao, activityDao: ActivityDao) {
            // Datos de muestra
            tripDao.insertTrip(TripEntity(tripId = 1, title = "Escapada a París", description = "Ciudad del amor", startDate = "12/03/2025", endDate = "16/03/2025", destineCity = "París", departureCity = "Madrid", flight = "IB3456", price = 248, hotelName = "Hotel Roquefort Palace", status = "COMPLETED", imageEmoji = "🗼"))
            tripDao.insertTrip(TripEntity(tripId = 2, title = "Tokio Express", description = "Aventura japonesa", startDate = "28/06/2025", endDate = "05/07/2025", destineCity = "Tokio", departureCity = "Barcelona", flight = "JL7712", price = 680, hotelName = "Madriguera Boutique", status = "COMPLETED", imageEmoji = "🗾"))
            tripDao.insertTrip(TripEntity(tripId = 3, title = "Roma Eterna", description = "Historia italiana", startDate = "05/08/2025", endDate = "10/08/2025", destineCity = "Roma", departureCity = "Madrid", flight = "VY1234", price = 193, hotelName = "Cloaca Suites", status = "COMPLETED", imageEmoji = "🏛️"))
            tripDao.insertTrip(TripEntity(tripId = 4, title = "Nueva York en otoño", description = "La gran manzana", startDate = "20/11/2025", endDate = "27/11/2025", destineCity = "Nueva York", departureCity = "Valencia", flight = "IB0091", price = 590, hotelName = "Alcantarilla Inn", status = "PENDING", imageEmoji = "🗽"))
            tripDao.insertTrip(TripEntity(tripId = 5, title = "Londres de invierno", description = "Mercados navideños", startDate = "14/01/2026", endDate = "18/01/2026", destineCity = "Londres", departureCity = "Sevilla", flight = "BA2341", price = 312, hotelName = "Madriguera Boutique", status = "PENDING", imageEmoji = "🎡"))
            tripDao.insertTrip(TripEntity(tripId = 6, title = "Ámsterdam en primavera", description = "Tulipanes y canales", startDate = "03/03/2026", endDate = "07/03/2026", destineCity = "Ámsterdam", departureCity = "Madrid", flight = "VY5566", price = 275, hotelName = "Canal Rat Hotel", status = "PENDING", imageEmoji = "🌷"))
            tripDao.insertTrip(TripEntity(tripId = 7, title = "Lisboa escapada", description = "Fados y pasteles", startDate = "19/04/2026", endDate = "22/04/2026", destineCity = "Lisboa", departureCity = "Barcelona", flight = "TP8823", price = 130, hotelName = "Tejo Suites", status = "CANCELLED", imageEmoji = "🇵🇹"))

            activityDao.insertActivity(ActivityEntity(tripId = 4, title = "Visita a Times Square", description = "Paseo por el corazón de Manhattan", date = "2025-11-21", time = "10:00"))
            activityDao.insertActivity(ActivityEntity(tripId = 4, title = "Central Park", description = "Picnic en el parque más famoso", date = "2025-11-22", time = "12:30"))
            activityDao.insertActivity(ActivityEntity(tripId = 4, title = "Museo de Arte Moderno", description = "Visita al MoMA", date = "2025-11-23", time = "09:00"))
            activityDao.insertActivity(ActivityEntity(tripId = 5, title = "Torre de Londres", description = "Visita histórica", date = "2026-01-15", time = "11:00"))
        }
    }
}