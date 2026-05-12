package com.example.alcantarilla_trips.data.remote.model

import com.google.gson.annotations.SerializedName

// ── Búsqueda de hoteles ───────────────────────────────────────────────────────

data class HotelSearchResponse(
    @SerializedName("result")  val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("data")    val data: HotelSearchData?
)

data class HotelSearchData(
    @SerializedName("body") val body: HotelSearchBody?
)

data class HotelSearchBody(
    @SerializedName("searchResults") val searchResults: HotelSearchResults?
)

data class HotelSearchResults(
    @SerializedName("totalCount") val totalCount: Int = 0,
    @SerializedName("results")    val results: List<HotelDto> = emptyList()
)

data class HotelDto(
    @SerializedName("id")                 val id: String,
    @SerializedName("name")               val name: String,
    @SerializedName("starRating")         val starRating: Double = 0.0,
    @SerializedName("address")            val address: HotelAddressDto?,
    @SerializedName("guestReviews")       val guestReviews: GuestReviewsDto?,
    @SerializedName("ratePlan")           val ratePlan: RatePlanDto?,
    @SerializedName("optimizedThumbUrls") val thumbUrls: ThumbUrlsDto?,
    @SerializedName("urls")               val urls: HotelUrlsDto?
)

data class HotelAddressDto(
    @SerializedName("streetAddress") val streetAddress: String?,
    @SerializedName("locality")      val locality: String?,
    @SerializedName("countryName")   val countryName: String?
)

data class GuestReviewsDto(
    @SerializedName("rating") val rating: String?,
    @SerializedName("scale")  val scale: String?,
    @SerializedName("total")  val total: String?
)

data class RatePlanDto(
    @SerializedName("price") val price: RatePriceDto?
)

data class RatePriceDto(
    @SerializedName("current")      val current: String?,
    @SerializedName("exactCurrent") val exactCurrent: Double?
)

data class ThumbUrlsDto(
    @SerializedName("srpDesktop") val srpDesktop: String?
)

data class HotelUrlsDto(
    @SerializedName("hotelImages") val hotelImages: List<HotelImageDto>?
)

data class HotelImageDto(
    @SerializedName("baseUrl")     val baseUrl: String?,
    @SerializedName("caption")     val caption: String?
)

// ── Búsqueda de ubicación ─────────────────────────────────────────────────────

data class HotelLocationResponse(
    @SerializedName("suggestions") val suggestions: List<HotelLocationSuggestion> = emptyList()
)

data class HotelLocationSuggestion(
    @SerializedName("group")    val group: String?,
    @SerializedName("entities") val entities: List<HotelLocationEntity> = emptyList()
)

data class HotelLocationEntity(
    @SerializedName("destinationId") val destinationId: String,
    @SerializedName("type")          val type: String?,
    @SerializedName("caption")       val caption: String?,
    @SerializedName("name")          val name: String?
)

// ── Detalle del hotel (rooms) ─────────────────────────────────────────────────

data class HotelDetailResponse(
    @SerializedName("data") val data: HotelDetailData?
)

data class HotelDetailData(
    @SerializedName("body") val body: HotelDetailBody?
)

data class HotelDetailBody(
    @SerializedName("propertyDescription") val propertyDescription: PropertyDescriptionDto?,
    @SerializedName("roomsAndRates")        val roomsAndRates: RoomsAndRatesDto?
)

data class PropertyDescriptionDto(
    @SerializedName("name")        val name: String?,
    @SerializedName("address")     val address: HotelAddressDto?,
    @SerializedName("starRating")  val starRating: Double?,
    @SerializedName("featuredImageUrl") val featuredImageUrl: FeaturedImageDto?,
    @SerializedName("images")      val images: List<HotelImageDto>?
)

data class FeaturedImageDto(
    @SerializedName("url") val url: String?
)

data class RoomsAndRatesDto(
    @SerializedName("rooms") val rooms: List<RoomDto>?
)

data class RoomDto(
    @SerializedName("roomKey")         val roomKey: String?,
    @SerializedName("name")            val name: String?,
    @SerializedName("images")          val images: List<HotelImageDto>?,
    @SerializedName("ratePlans")       val ratePlans: List<RoomRatePlanDto>?
)

data class RoomRatePlanDto(
    @SerializedName("price")       val price: RatePriceDto?,
    @SerializedName("features")    val features: RoomFeaturesDto?
)

data class RoomFeaturesDto(
    @SerializedName("breakfast") val breakfast: Boolean?,
    @SerializedName("freeCancellation") val freeCancellation: Boolean?
)