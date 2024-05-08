package com.example.parkease

import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("key") apiKey: String,
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") placeType: String = "train_station" // Adjust this to the desired type
    ): NearbySearchResponse
}
