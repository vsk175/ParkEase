package com.example.parkease

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParkingPlace(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Auto-generate primary key
    val name: String, // Name of the parking place
    val address: String, // Physical address
    val latitude: Double, // Latitude coordinate of the parking spot
    val longitude: Double, // Longitude coordinate of the parking spot
    val availableSpots: Int, // Number of available spots
    val totalSpots: Int // Total number of spots
)
