package com.example.parkease

import android.app.Application
import kotlinx.coroutines.flow.Flow

class ParkingRepository (application: Application) {
    private var parkingDao: ParkingDAO = ParkingDatabase.getDatabase(application).parkingDao()

    val allParkingPlaces: Flow<List<ParkingPlace>> = parkingDao.getAllParkingPlaces()

    suspend fun insert(parkingPlace: ParkingPlace) {
        parkingDao.insertParkingPlace(parkingPlace)
    }

    suspend fun deleteAllParkingPlaces() {
        parkingDao.deleteAllParkingPlaces()
    }
}
