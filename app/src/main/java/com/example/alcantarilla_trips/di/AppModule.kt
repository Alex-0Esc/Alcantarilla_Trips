package com.example.alcantarilla_trips.di // Ajusta a tu paquete real

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // Si más adelante necesitas Firestore o Realtime Database, los añadirías aquí:
    // @Provides
    // @Singleton
    // fun provideFirestore() = FirebaseFirestore.getInstance()
}