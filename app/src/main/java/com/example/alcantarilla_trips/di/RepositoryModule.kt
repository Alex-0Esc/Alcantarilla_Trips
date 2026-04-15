package com.example.alcantarilla_trips.di

import com.example.alcantarilla_trips.data.repository.ActivityRepositoryImpl
import com.example.alcantarilla_trips.data.repository.TripRepositoryImpl
import com.example.alcantarilla_trips.domain.ActivityRepository
import com.example.alcantarilla_trips.domain.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTripRepository(
        tripRepositoryImpl: TripRepositoryImpl
    ): TripRepository

    @Binds
    @Singleton
    abstract fun bindActivityRepository(
        activityRepositoryImpl: ActivityRepositoryImpl
    ): ActivityRepository
}