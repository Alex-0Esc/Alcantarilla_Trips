package com.example.alcantarilla_trips.domain
import android.app.Activity

data class Trip(
    val id: String,
    val title: String,
    val destination: String,
    val budget: Double,
    val activities: List<Activity>
) {

    /**
     * Returns the total cost of all activities in the trip.
     */
    fun getTotalActivityCost(): Double {
        // TODO: calculate and return the sum of all activity costs
        return 0.0
    }

    /**
     * Checks whether the trip is currently over budget.
     */
    fun isOverBudget(): Boolean {
        // TODO: determine if total activity cost exceeds the trip budget
        return false
    }

    /**
     * Adds a new activity to the trip.
     */
    fun addActivity(activity: Activity) {
        // TODO: implement logic to add an activity to the trip
    }

    /**
     * Removes an activity from the trip.
     */
    fun removeActivity(activityId: String) {
        // TODO: implement logic to remove an activity by its id
    }

    /**
     * Calculates the average daily spending for the trip.
     */
    fun calculateAverageDailySpending(): Double {
        // TODO: implement average spending calculation based on trip duration
        return 0.0
    }

    /**
     * Suggests activities that fit within the remaining budget.
     */
    fun suggestAffordableActivities(): List<Activity> {
        // TODO: implement recommendation logic based on remaining budget
        return emptyList()
    }

    /**
     * Optimizes the distribution of activities across days.
     */
    fun optimizeActivitySchedule() {
        // TODO: implement scheduling optimization algorithm
    }

    /**
     * Exports the trip summary as a formatted string.
     */
    fun exportTripSummary(): String {
        // TODO: generate and return a formatted summary of the trip
        return ""
    }

    /**
     * Validates the trip data before saving.
     */
    fun validateTrip(): Boolean {
        // TODO: implement validation rules (budget > 0, title not empty, etc.)
        return false
    }
}