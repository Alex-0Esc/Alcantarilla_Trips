package com.example.alcantarilla_trips.di

import com.example.alcantarilla_trips.data.repository.ActivityRepositoryImpl
import com.example.alcantarilla_trips.data.repository.HotelRepositoryImpl
import com.example.alcantarilla_trips.data.repository.TripRepositoryImpl
import com.example.alcantarilla_trips.domain.ActivityRepository
import com.example.alcantarilla_trips.domain.HotelRepository
import com.example.alcantarilla_trips.domain.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindTripRepository(impl: TripRepositoryImpl): TripRepository

    @Binds @Singleton
    abstract fun bindActivityRepository(impl: ActivityRepositoryImpl): ActivityRepository

    // T1.3: Repositorio de hoteles
    @Binds @Singleton
    abstract fun bindHotelRepository(impl: HotelRepositoryImpl): HotelRepository
}