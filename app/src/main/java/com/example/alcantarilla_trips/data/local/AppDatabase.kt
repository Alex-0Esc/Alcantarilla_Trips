package com.example.alcantarilla_trips.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.alcantarilla_trips.data.local.dao.*
import com.example.alcantarilla_trips.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TripEntity::class,
        ActivityEntity::class,
        UserEntity::class,
        BookingEntity::class,
        AccessLogEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao
    abstract fun bookingDao(): BookingDao
    abstract fun accessLogDao(): AccessLogDao

    companion object {
        const val DATABASE_NAME = "alcantarilla_trips_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun buildDatabase(context: Context): AppDatabase {
            // Use the Elvis operator to return instance if it exists,
            // or enter the synchronized block to create it.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, // Explicitly tells Room the class type
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // We use a Coroutine to populate the DB after creation
                            CoroutineScope(Dispatchers.IO).launch {
                                // Note: We call buildDatabase here to get the instance safely
                                val database = buildDatabase(context)
                                populateDatabase(database.tripDao(), database.activityDao())
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance // This now correctly returns AppDatabase
            }
        }

        private suspend fun populateDatabase(tripDao: TripDao, activityDao: ActivityDao) {
            // Your existing population code...
            tripDao.insertTrip(TripEntity(tripId = 1, title = "Escapada a París", description = "Ciudad del amor", startDate = "12/03/2025", endDate = "16/03/2025", destineCity = "París", departureCity = "Madrid", flight = "IB3456", price = 248, hotelName = "Hotel Roquefort Palace", status = "COMPLETED", imageEmoji = "🗼"))
            // ... (rest of your trips and activities)
        }
    }
}