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
}