package com.example.parkease

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Locale

class PlacesViewModel : ViewModel() {
    private val repository = PlacesRepository()
    val placesResponse: MutableState<NearbySearchResponse> = mutableStateOf(NearbySearchResponse())
    val errorMessage: MutableState<String?> = mutableStateOf(null)
    var searchedLocation: Location? = null


    fun fetchNearbyPlacesByName(context: Context, placeName: String, radius: Int) {
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                // Get the first matching address
                val addresses = geocoder.getFromLocationName(placeName, 1)
                if (!addresses.isNullOrEmpty()) {
                    val location = addresses[0]
                    val latLng = "${location.latitude},${location.longitude}"

                    // Store the searched location coordinates
                    searchedLocation = Location("").apply {
                        latitude  = location.latitude
                        longitude  = location.longitude
                    }


                    // Fetch nearby places using the coordinates
                    val response = repository.getNearbyPlaces(latLng, radius)
                    placesResponse.value = response
                    errorMessage.value = null
                } else {
                    // Handle no location found case
                    errorMessage.value = "Location not found for '$placeName'. Please try another name."
                    placesResponse.value = NearbySearchResponse()
                }
            } catch (e: Exception) {
                // Handle geocoding or network error
                errorMessage.value = "An error occurred during the search. Please try again."
                placesResponse.value = NearbySearchResponse()
            }
        }
    }

    // Calculate the distance from the searched location to another point
    fun calculateDistanceToPlace(lat: Double, lng: Double): Float? {
        val destination = Location("").apply {
            latitude = lat
            longitude = lng
        }
        return searchedLocation?.distanceTo(destination)
    }
}



