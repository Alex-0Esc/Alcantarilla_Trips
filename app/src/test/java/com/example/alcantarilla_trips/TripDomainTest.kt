package com.example.alcantarilla_trips

import com.example.alcantarilla_trips.domain.Trip
import com.example.alcantarilla_trips.domain.TripStatus
import com.example.alcantarilla_trips.domain.validateTrip
import com.example.alcantarilla_trips.domain.exportTripSummary
import com.example.alcantarilla_trips.domain.isOverBudget
import org.junit.Assert.*
import org.junit.Test
import com.example.alcantarilla_trips.domain.areDatesValid
import com.example.alcantarilla_trips.domain.isTripNameDuplicate

class TripDomainTest {

    // Trip de ejemplo basado en tu modelo real de la T1
    private val sampleTrip = Trip(
        tripId = 1,
        title = "Viaje a Paris",
        description = "Vacaciones",
        startDate = "01/06/2025",
        endDate = "07/06/2025",
        destineCity = "Paris",
        departureCity = "Madrid",
        status = TripStatus.PENDING,
        price = 800,
        flight = "IB1234",
        hotelName = "Hotel Lumiere",
        imageEmoji = "🗼"
    )

    // --- Tests de Validación (T1.5 y T5.2) ---

    @Test
    fun validateTrip_returnsFalseWhenTitleIsEmpty() {
        val trip = sampleTrip.copy(title = "")
        val result = validateTrip(trip)
        assertFalse("El viaje no debería ser válido con título vacío", result)
    }

    @Test
    fun validateTrip_returnsFalseWhenPriceIsNegative() {
        val trip = sampleTrip.copy(price = -100)
        val result = validateTrip(trip)
        assertFalse("El precio no puede ser negativo", result)
    }

    @Test
    fun validateTrip_returnsTrueForValidTrip() {
        val result = validateTrip(sampleTrip)
        assertTrue("El viaje debería ser válido con datos correctos", result)
    }

    // --- Tests de Lógica de Negocio (Funciones de Dominio) ---

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
    fun exportTripSummary_containsDestinationAndPrice() {
        val result = exportTripSummary(sampleTrip)
        assertTrue("El resumen debe contener el destino", result.contains("Paris"))
        assertTrue("El resumen debe contener el precio", result.contains("800"))
    }

    // --- Tests de Estructura de Datos (T1.2) ---

    @Test
    fun trip_defaultFieldsAreSetCorrectly() {
        val trip = Trip(
            tripId = 99,
            title = "Test",
            description = "Desc",
            startDate = "01/01/2025",
            endDate = "02/01/2025",
            destineCity = "Roma",
            departureCity = "Madrid"
        )
        // Verificamos que los valores por defecto del constructor funcionen
        assertEquals("🏙️", trip.imageEmoji)
        assertEquals(TripStatus.PENDING, trip.status)
        assertEquals(0, trip.price)
    }

    @Test
    fun trip_copyPreservesUnchangedFields() {
        val modified = sampleTrip.copy(title = "Nuevo Titulo")
        assertEquals(sampleTrip.destineCity, modified.destineCity)
        assertEquals("Nuevo Titulo", modified.title)
    }
    @Test
    fun `isTripNameDuplicate returns true when name exists`() {
        val trips = listOf(sampleTrip)
        assertTrue(isTripNameDuplicate("Viaje a Paris", trips))
    }

    @Test
    fun `isTripNameDuplicate returns false for new name`() {
        val trips = listOf(sampleTrip)
        assertFalse(isTripNameDuplicate("Viaje a Roma", trips))
    }

    @Test
    fun `areDatesValid returns false when end before start`() {
        assertFalse(areDatesValid("10/06/2025", "05/06/2025"))
    }

    @Test
    fun `areDatesValid returns true when end after start`() {
        assertTrue(areDatesValid("01/06/2025", "07/06/2025"))
    }

    @Test
    fun `areDatesValid returns true when same day`() {
        assertTrue(areDatesValid("01/06/2025", "01/06/2025"))
    }

}