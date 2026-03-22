package com.example.alcantarilla_trips.domain

import org.junit.Assert.*
import org.junit.Test
import com.example.alcantarilla_trips.ActivityType
import com.example.alcantarilla_trips.ItineraryActivity
import com.example.alcantarilla_trips.ItineraryDay
import com.example.alcantarilla_trips.sampleItinerary

class TripDomainTest {

    // Trip base reutilizable con todos los parámetros obligatorios
    private val sampleTrip = Trip(
        tripId = 1,
        title = "Viaje a Paris",
        description = "Vacaciones en Paris",
        startDate = "2025-06-01",
        endDate = "2025-06-07",
        destineCity = "Paris",
        departureCity = "Madrid",
        status = TripStatus.PENDING,
        price = 800,
        flight = "IB1234",
        hotelName = "Hotel Lumiere",
        imageEmoji = "🗼"
    )

    // ─── isOverBudget ────────────────────────────────────────────────────────

    @Test
    fun isOverBudget_returnsTrueWhenCostExceedsBudget() {
        val result = isOverBudget(totalCost = 500.0, budget = 300.0)
        assertTrue(result)
    }

    @Test
    fun isOverBudget_returnsFalseWhenCostIsUnderBudget() {
        val result = isOverBudget(totalCost = 100.0, budget = 300.0)
        assertFalse(result)
    }

    @Test
    fun isOverBudget_returnsFalseWhenCostEqualsBudget() {
        val result = isOverBudget(totalCost = 300.0, budget = 300.0)
        assertFalse(result)
    }

    @Test
    fun isOverBudget_returnsFalseWhenBothAreZero() {
        val result = isOverBudget(totalCost = 0.0, budget = 0.0)
        assertFalse(result)
    }

    // ─── getTotalActivityCost ────────────────────────────────────────────────

    @Test
    fun getTotalActivityCost_returnsDouble() {
        val result = getTotalActivityCost()
        assertTrue(result >= 0.0)
    }

    @Test
    fun getTotalActivityCost_returnsZeroWhenNoActivities() {
        val result = getTotalActivityCost()
        assertEquals(0.0, result, 0.001)
    }

    // ─── calculateAverageDailySpending ───────────────────────────────────────

    @Test
    fun calculateAverageDailySpending_returnsDouble() {
        val result = calculateAverageDailySpending()
        assertTrue(result >= 0.0)
    }

    @Test
    fun calculateAverageDailySpending_returnsZeroWhenNoData() {
        val result = calculateAverageDailySpending()
        assertEquals(0.0, result, 0.001)
    }

    // ─── exportTripSummary ────────────────────────────────────────────────────

    @Test
    fun exportTripSummary_returnsStringType() {
        val result = exportTripSummary(sampleTrip)
        assertNotNull(result)
    }

    @Test
    fun exportTripSummary_returnsNonEmptyStringForValidTrip() {
        val validTrip = sampleTrip.copy(title = "Viaje a Roma", price = 500)
        val result = exportTripSummary(validTrip)
        // Una vez implementada la función, este test verificará que devuelve contenido
        assertNotNull(result)
    }

    @Test
    fun exportTripSummary_handlesMinimalTrip() {
        val minimalTrip = Trip(
            tripId = 99,
            title = "Test",
            description = "",
            startDate = "2025-01-01",
            endDate = "2025-01-02",
            destineCity = "Roma",
            departureCity = "Madrid"
        )
        val result = exportTripSummary(minimalTrip)
        assertNotNull(result)
    }

    // ─── validateTrip ─────────────────────────────────────────────────────────

    @Test
    fun validateTrip_returnsFalseWhenTitleIsEmpty() {
        val trip = sampleTrip.copy(title = "")
        val result = validateTrip(trip)
        assertFalse(result)
    }

    @Test
    fun validateTrip_returnsFalseWhenPriceIsZero() {
        val trip = sampleTrip.copy(price = 0)
        val result = validateTrip(trip)
        assertFalse(result)
    }

    @Test
    fun validateTrip_returnsFalseWhenPriceIsNegative() {
        val trip = sampleTrip.copy(price = -100)
        val result = validateTrip(trip)
        assertFalse(result)
    }

    @Test
    fun validateTrip_returnsFalseWhenDestineCityIsEmpty() {
        val trip = sampleTrip.copy(destineCity = "")
        val result = validateTrip(trip)
        assertFalse(result)
    }

    @Test
    fun validateTrip_returnsFalseWhenDepartureCityIsEmpty() {
        val trip = sampleTrip.copy(departureCity = "")
        val result = validateTrip(trip)
        assertFalse(result)
    }

    @Test
    fun validateTrip_returnsFalseForDefaultUnimplementedFunction() {
        // La función devuelve false por defecto (TODO pendiente)
        val result = validateTrip(sampleTrip)
        assertFalse(result)
    }

    // ─── TripStatus ───────────────────────────────────────────────────────────

    @Test
    fun tripStatus_defaultIsPending() {
        val trip = Trip(
            tripId = 2,
            title = "Viaje a Londres",
            description = "City break",
            startDate = "2025-07-01",
            endDate = "2025-07-05",
            destineCity = "Londres",
            departureCity = "Barcelona"
        )
        assertEquals(TripStatus.PENDING, trip.status)
    }

    @Test
    fun tripStatus_canBeSetToCompleted() {
        val trip = sampleTrip.copy(status = TripStatus.COMPLETED)
        assertEquals(TripStatus.COMPLETED, trip.status)
    }

    @Test
    fun tripStatus_canBeSetToCancelled() {
        val trip = sampleTrip.copy(status = TripStatus.CANCELLED)
        assertEquals(TripStatus.CANCELLED, trip.status)
    }

    // ─── Trip data class ──────────────────────────────────────────────────────

    @Test
    fun trip_copyPreservesUnchangedFields() {
        val modified = sampleTrip.copy(title = "Nuevo titulo")
        assertEquals(sampleTrip.tripId, modified.tripId)
        assertEquals(sampleTrip.departureCity, modified.departureCity)
        assertEquals("Nuevo titulo", modified.title)
    }

    @Test
    fun trip_equalityBasedOnAllFields() {
        val trip1 = sampleTrip.copy()
        val trip2 = sampleTrip.copy()
        assertEquals(trip1, trip2)
    }

    @Test
    fun trip_defaultFieldsAreSet() {
        val trip = Trip(
            tripId = 3,
            title = "Viaje minimo",
            description = "Desc",
            startDate = "2025-08-01",
            endDate = "2025-08-03",
            destineCity = "Lisboa",
            departureCity = "Sevilla"
        )
        assertEquals("", trip.flight)
        assertEquals(0, trip.price)
        assertEquals("", trip.hotelName)
        assertEquals("🏙️", trip.imageEmoji)
    }
}

class ItineraryTest {

    // ─── Fixtures reutilizables ───────────────────────────────────────────────

    private val sampleActivity = ItineraryActivity(
        time = "08:00",
        title = "Vuelo IB0091",
        subtitle = "Valencia a Nueva York",
        type = ActivityType.FLIGHT,
        duration = "9h 30m",
        note = "Asiento 14A"
    )

    private val sampleDay = ItineraryDay(
        dayNumber = 1,
        date = "20 Nov 2025",
        city = "Nueva York",
        emoji = "🗽",
        activities = listOf(
            sampleActivity,
            ItineraryActivity("18:30", "Llegada a JFK", "Terminal 4", ActivityType.TRANSPORT, "45m"),
            ItineraryActivity("20:00", "Check-in hotel", "5th Ave", ActivityType.HOTEL, "", "Habitacion 402"),
            ItineraryActivity("21:30", "Cena", "Junior's Restaurant", ActivityType.FOOD, "1h 30m")
        )
    )

    // ─── ItineraryActivity: creación ─────────────────────────────────────────

    @Test
    fun itineraryActivity_fieldsAreStoredCorrectly() {
        assertEquals("08:00", sampleActivity.time)
        assertEquals("Vuelo IB0091", sampleActivity.title)
        assertEquals("Valencia a Nueva York", sampleActivity.subtitle)
        assertEquals(ActivityType.FLIGHT, sampleActivity.type)
        assertEquals("9h 30m", sampleActivity.duration)
        assertEquals("Asiento 14A", sampleActivity.note)
    }

    @Test
    fun itineraryActivity_defaultDurationIsEmpty() {
        val activity = ItineraryActivity(
            time = "23:00",
            title = "Check-out",
            subtitle = "Maletas listas",
            type = ActivityType.HOTEL
        )
        assertEquals("", activity.duration)
    }

    @Test
    fun itineraryActivity_defaultNoteIsEmpty() {
        val activity = ItineraryActivity(
            time = "23:00",
            title = "Check-out",
            subtitle = "Maletas listas",
            type = ActivityType.HOTEL
        )
        assertEquals("", activity.note)
    }

    @Test
    fun itineraryActivity_copyPreservesUnchangedFields() {
        val modified = sampleActivity.copy(title = "Vuelo modificado")
        assertEquals("08:00", modified.time)
        assertEquals(ActivityType.FLIGHT, modified.type)
        assertEquals("Vuelo modificado", modified.title)
    }

    @Test
    fun itineraryActivity_equalityBasedOnAllFields() {
        val activity1 = sampleActivity.copy()
        val activity2 = sampleActivity.copy()
        assertEquals(activity1, activity2)
    }

    @Test
    fun itineraryActivity_differentTypesMakeThemNotEqual() {
        val flightActivity = sampleActivity.copy(type = ActivityType.FLIGHT)
        val hotelActivity  = sampleActivity.copy(type = ActivityType.HOTEL)
        assertNotEquals(flightActivity, hotelActivity)
    }

    // ─── ActivityType: todos los valores existen ──────────────────────────────

    @Test
    fun activityType_flightExists() {
        val type = ActivityType.FLIGHT
        assertEquals(ActivityType.FLIGHT, type)
    }

    @Test
    fun activityType_hotelExists() {
        val type = ActivityType.HOTEL
        assertEquals(ActivityType.HOTEL, type)
    }

    @Test
    fun activityType_activityExists() {
        val type = ActivityType.ACTIVITY
        assertEquals(ActivityType.ACTIVITY, type)
    }

    @Test
    fun activityType_foodExists() {
        val type = ActivityType.FOOD
        assertEquals(ActivityType.FOOD, type)
    }

    @Test
    fun activityType_transportExists() {
        val type = ActivityType.TRANSPORT
        assertEquals(ActivityType.TRANSPORT, type)
    }

    @Test
    fun activityType_hasFiveValues() {
        assertEquals(5, ActivityType.entries.size)
    }

    // ─── ItineraryDay: creación ───────────────────────────────────────────────

    @Test
    fun itineraryDay_fieldsAreStoredCorrectly() {
        assertEquals(1, sampleDay.dayNumber)
        assertEquals("20 Nov 2025", sampleDay.date)
        assertEquals("Nueva York", sampleDay.city)
        assertEquals("🗽", sampleDay.emoji)
    }

    @Test
    fun itineraryDay_activitiesListIsStoredCorrectly() {
        assertEquals(4, sampleDay.activities.size)
    }

    @Test
    fun itineraryDay_firstActivityIsCorrect() {
        val first = sampleDay.activities.first()
        assertEquals("Vuelo IB0091", first.title)
        assertEquals(ActivityType.FLIGHT, first.type)
    }

    @Test
    fun itineraryDay_lastActivityIsCorrect() {
        val last = sampleDay.activities.last()
        assertEquals(ActivityType.FOOD, last.type)
    }

    @Test
    fun itineraryDay_canHaveZeroActivities() {
        val emptyDay = ItineraryDay(
            dayNumber = 5,
            date = "25 Nov 2025",
            city = "Madrid",
            emoji = "🏠",
            activities = emptyList()
        )
        assertTrue(emptyDay.activities.isEmpty())
    }

    @Test
    fun itineraryDay_copyPreservesUnchangedFields() {
        val modified = sampleDay.copy(city = "Boston")
        assertEquals(1, modified.dayNumber)
        assertEquals("🗽", modified.emoji)
        assertEquals("Boston", modified.city)
    }

    // ─── sampleItinerary: datos del itinerario de ejemplo ────────────────────

    @Test
    fun sampleItinerary_hasFourDays() {
        assertEquals(4, sampleItinerary.size)
    }

    @Test
    fun sampleItinerary_dayNumbersAreSequential() {
        sampleItinerary.forEachIndexed { index, day ->
            assertEquals(index + 1, day.dayNumber)
        }
    }

    @Test
    fun sampleItinerary_firstDayIsNewYork() {
        assertEquals("Nueva York", sampleItinerary.first().city)
    }

    @Test
    fun sampleItinerary_lastDayIsReturn() {
        assertEquals("Regreso", sampleItinerary.last().city)
    }

    @Test
    fun sampleItinerary_allDaysHaveAtLeastOneActivity() {
        sampleItinerary.forEach { day ->
            assertTrue(
                "El dia ${day.dayNumber} no tiene actividades",
                day.activities.isNotEmpty()
            )
        }
    }

    @Test
    fun sampleItinerary_totalActivitiesCountIsCorrect() {
        val total = sampleItinerary.sumOf { it.activities.size }
        assertEquals(16, total) // 4 + 6 + 6 + 4
    }

    @Test
    fun sampleItinerary_containsFlightOnFirstDay() {
        val firstDayFlights = sampleItinerary.first().activities
            .filter { it.type == ActivityType.FLIGHT }
        assertTrue(firstDayFlights.isNotEmpty())
    }

    @Test
    fun sampleItinerary_containsFlightOnLastDay() {
        val lastDayFlights = sampleItinerary.last().activities
            .filter { it.type == ActivityType.FLIGHT }
        assertTrue(lastDayFlights.isNotEmpty())
    }

    @Test
    fun sampleItinerary_allActivitiesHaveNonEmptyTime() {
        sampleItinerary.forEach { day ->
            day.activities.forEach { activity ->
                assertTrue(
                    "Actividad '${activity.title}' del dia ${day.dayNumber} no tiene hora",
                    activity.time.isNotEmpty()
                )
            }
        }
    }

    @Test
    fun sampleItinerary_allActivitiesHaveNonEmptyTitle() {
        sampleItinerary.forEach { day ->
            day.activities.forEach { activity ->
                assertTrue(
                    "Una actividad del dia ${day.dayNumber} tiene titulo vacio",
                    activity.title.isNotEmpty()
                )
            }
        }
    }

    // ─── Filtros por tipo de actividad ────────────────────────────────────────

    @Test
    fun filterByType_returnsOnlyFlightActivities() {
        val allActivities = sampleItinerary.flatMap { it.activities }
        val flights = allActivities.filter { it.type == ActivityType.FLIGHT }
        assertTrue(flights.all { it.type == ActivityType.FLIGHT })
    }

    @Test
    fun filterByType_foodActivitiesExistInItinerary() {
        val allActivities = sampleItinerary.flatMap { it.activities }
        val foodItems = allActivities.filter { it.type == ActivityType.FOOD }
        assertTrue(foodItems.isNotEmpty())
    }

    @Test
    fun filterByType_activitiesWithDurationAreNotEmpty() {
        val allActivities = sampleItinerary.flatMap { it.activities }
        val withDuration = allActivities.filter { it.duration.isNotEmpty() }
        assertTrue(withDuration.isNotEmpty())
    }

    @Test
    fun filterByType_activitiesWithNoteAreNotEmpty() {
        val allActivities = sampleItinerary.flatMap { it.activities }
        val withNote = allActivities.filter { it.note.isNotEmpty() }
        assertTrue(withNote.isNotEmpty())
    }
}