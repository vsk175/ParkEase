package com.example.parkease

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ParkingPlace::class,
            parentColumns = ["id"],
            childColumns = ["placeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val placeId: Int, // ID of the associated parking place
    val userId: String, // ID of the user making the booking
    @TypeConverters(Converters::class)
    val bookingDate: Date, // Date of the booking
    @TypeConverters(Converters::class)
    val bookingTime: Date, // Time of the booking
    val durationHours: Int, // Duration of the booking in hours
    val status: BookingStatus // Status of the booking
)

enum class BookingStatus {
    ACTIVE,
    CANCELLED,
    COMPLETED
}

