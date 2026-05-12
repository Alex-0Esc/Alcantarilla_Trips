package com.example.alcantarilla_trips.di

import android.content.Context
import com.example.alcantarilla_trips.data.local.AppDatabase
import com.example.alcantarilla_trips.data.local.dao.AccessLogDao
import com.example.alcantarilla_trips.data.local.dao.ActivityDao
import com.example.alcantarilla_trips.data.local.dao.TripDao
import com.example.alcantarilla_trips.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.alcantarilla_trips.data.local.dao.BookingDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.buildDatabase(context)
    }

    @Provides
    fun provideTripDao(database: AppDatabase): TripDao = database.tripDao()

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideActivityDao(database: AppDatabase): ActivityDao = database.activityDao()

    @Provides
    fun provideAccessLogDao(database: AppDatabase): AccessLogDao = database.accessLogDao()

    // --- ADD THIS METHOD TO FIX THE ERROR ---
    @Provides
    fun provideBookingDao(database: AppDatabase): BookingDao {
        return database.bookingDao()
    }
}