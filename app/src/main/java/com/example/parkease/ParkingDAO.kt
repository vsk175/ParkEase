package com.example.parkease

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParkingDAO {

    @Query("SELECT * FROM ParkingPlace")
    fun getAllParkingPlaces(): Flow<List<ParkingPlace>>

    @Insert
    suspend fun insertParkingPlace(parkingPlace: ParkingPlace)

    @Query("DELETE FROM ParkingPlace")
    suspend fun deleteAllParkingPlaces()

    @Query("SELECT * FROM Booking")
    fun getAllBookings(): Flow<List<Booking>>

    @Query("SELECT * FROM Booking WHERE userId = :userId")
    fun getBookingsByUserId(userId: String): Flow<List<Booking>>

    @Insert
    suspend fun insertBooking(booking: Booking)

    @Query("DELETE FROM Booking")
    suspend fun deleteAllBookings()
}