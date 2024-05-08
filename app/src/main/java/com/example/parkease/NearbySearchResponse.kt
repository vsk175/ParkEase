package com.example.parkease

data class NearbySearchResponse(
    val results: List<PlaceResult> = emptyList()
)

data class PlaceResult(
    val name: String,
    val vicinity: String,
    val geometry: Geometry
)

data class Geometry(val location: PlaceLocation)

data class PlaceLocation(val lat: Double, val lng: Double)
