package com.example.parkease

class PlacesRepository {
    private val placesService = PlacesRetrofitObject.retrofitService
    private val API_KEY = "AIzaSyDV2jwSautWcQkKPSQ6eOK6ACbEIYc5cgs"

    suspend fun getNearbyPlaces(location: String, radius: Int): NearbySearchResponse {
        return placesService.getNearbyPlaces(API_KEY, location, radius)
    }
}
