package com.example.alcantarilla_trips

import com.example.alcantarilla_trips.data.remote.HotelApiService
import com.example.alcantarilla_trips.data.remote.model.*
import com.example.alcantarilla_trips.data.repository.HotelRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class HotelRepositoryTest {

    // Mock del servicio Retrofit — NO hace llamadas de red reales
    private val apiService: HotelApiService = mockk()
    private lateinit var repository: HotelRepositoryImpl

    // --- Datos de muestra ---
    private val fakeLocationResponse = HotelLocationResponse(
        suggestions = listOf(
            HotelLocationSuggestion(
                group = "CITY_GROUP",
                entities = listOf(
                    HotelLocationEntity(
                        destinationId = "2114",
                        type = "CITY",
                        caption = "París, Francia",
                        name = "París"
                    )
                )
            )
        )
    )

    private val fakeHotel = HotelDto(
        id         = "hotel_1",
        name       = "Hotel Eiffel Palace",
        starRating = 4.0,
        address    = HotelAddressDto("Rue de Rivoli 10", "París", "Francia"),
        guestReviews = GuestReviewsDto("8.5", "10", "1200"),
        ratePlan   = RatePlanDto(RatePriceDto("120 €", 120.0)),
        thumbUrls  = ThumbUrlsDto("https://example.com/hotel.jpg")
    )

    private val fakeSearchResponse = HotelSearchResponse(
        result  = "OK",
        message = null,
        data    = HotelSearchData(
            body = HotelSearchBody(
                searchResults = HotelSearchResults(
                    totalCount = 1,
                    results    = listOf(fakeHotel)
                )
            )
        )
    )

    @Before
    fun setUp() {
        repository = HotelRepositoryImpl(apiService)
    }

    // T1.4 — Test 1: Búsqueda correcta devuelve lista de hoteles
    @Test
    fun searchHotels_success() = runTest {
        // Arrange: simular respuestas exitosas
        coEvery { apiService.searchLocation(any(), any()) } returns
                Response.success(fakeLocationResponse)
        coEvery { apiService.searchHotels(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns
                Response.success(fakeSearchResponse)

        // Act
        val result = repository.searchHotels("París", "2025-06-01", "2025-06-05")

        // Assert
        assertTrue(result.isSuccess)
        val hotels = result.getOrThrow()
        assertEquals(1, hotels.size)
        assertEquals("Hotel Eiffel Palace", hotels[0].name)
        assertEquals(120.0, hotels[0].priceExact, 0.0)
    }

    // T1.4 — Test 2: Si no se encuentra la ciudad, devuelve failure
    @Test
    fun searchHotels_noReturn() = runTest {
        // Arrange: la API no devuelve ninguna sugerencia
        coEvery { apiService.searchLocation(any(), any()) } returns
                Response.success(HotelLocationResponse(suggestions = emptyList()))

        // Act
        val result = repository.searchHotels("CiudadInexistente", "2025-06-01", "2025-06-05")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("CiudadInexistente") == true)

        // Verificar que NO se llama al segundo endpoint si el primero falla
        coVerify(exactly = 0) { apiService.searchHotels(any(), any(), any(), any(), any(), any(), any(), any(), any()) }
    }

    // T1.4 — Test 3: Error HTTP en la búsqueda de ubicación
    @Test
    fun searchHotels_403() = runTest {
        // Arrange: la API devuelve 403 Forbidden
        coEvery { apiService.searchLocation(any(), any()) } returns
                Response.error(403, okhttp3.ResponseBody.create(null, "Forbidden"))

        // Act
        val result = repository.searchHotels("París", "2025-06-01", "2025-06-05")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("403") == true)
    }

    // T1.4 — Test 4: Error HTTP en la búsqueda de hoteles
    @Test
    fun searchHotels_failure() = runTest {
        // Arrange: location OK, hotels falla con 500
        coEvery { apiService.searchLocation(any(), any()) } returns
                Response.success(fakeLocationResponse)
        coEvery { apiService.searchHotels(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns
                Response.error(500, okhttp3.ResponseBody.create(null, "Internal Server Error"))

        // Act
        val result = repository.searchHotels("París", "2025-06-01", "2025-06-05")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("500") == true)
    }

    // T1.4 — Test 5: Excepción de red (sin conexión)
    @Test
    fun searchHotels_networkError() = runTest {
        // Arrange: simular timeout / sin conexión
        coEvery { apiService.searchLocation(any(), any()) } throws
                java.io.IOException("No internet connection")

        // Act
        val result = repository.searchHotels("París", "2025-06-01", "2025-06-05")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is java.io.IOException)
    }

    // T1.4 — Test 6: La lista viene vacía → resultado vacío sin error
    @Test
    fun searchHotels_emptyResult() = runTest {
        // Arrange: respuesta OK pero sin resultados
        val emptySearchResponse = fakeSearchResponse.copy(
            data = HotelSearchData(
                body = HotelSearchBody(
                    searchResults = HotelSearchResults(totalCount = 0, results = emptyList())
                )
            )
        )
        coEvery { apiService.searchLocation(any(), any()) } returns
                Response.success(fakeLocationResponse)
        coEvery { apiService.searchHotels(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns
                Response.success(emptySearchResponse)

        // Act
        val result = repository.searchHotels("París", "2025-06-01", "2025-06-05")

        // Assert
        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }
}