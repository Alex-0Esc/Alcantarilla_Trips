package com.example.alcantarilla_trips.domain

// Funciones auxiliares del dominio de Trip (pendientes de implementar)
fun getTotalActivityCost(): Double {
    // TODO: calculate and return the sum of all activity costs
    return 0.0
}

fun isOverBudget(totalCost: Double, budget: Double): Boolean {
    // TODO: determine if total activity cost exceeds the trip budget
    return false
}

fun calculateAverageDailySpending(): Double {
    // TODO: implement average spending calculation based on trip duration
    return 0.0
}

fun exportTripSummary(trip: Trip): String {
    // TODO: generate and return a formatted summary of the trip
    return ""
}

fun validateTrip(trip: Trip): Boolean {
    // TODO: implement validation rules (budget > 0, title not empty, etc.)
    return false
}