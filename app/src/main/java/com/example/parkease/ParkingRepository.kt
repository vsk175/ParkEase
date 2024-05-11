package com.example.parkease

import android.app.Application
import kotlinx.coroutines.flow.Flow

class ParkingRepository (application: Application) {

    private var parkingDao: ParkingDAO = ParkingDatabase.getDatabase(application).parkingDao()

    val allParkingPlaces: Flow<List<ParkingPlace>> = parkingDao.getAllParkingPlaces()
    val allAvailableParkingPlaces: Flow<List<ParkingPlace>> = parkingDao.getParkingPlacesWithAvailableSpots()
    fun getBookingsByUserId(userId: String): Flow<List<Booking>> {
        return parkingDao.getBookingsByUserId(userId)
    }
    suspend fun insert(parkingPlace: ParkingPlace) {
        parkingDao.insertParkingPlace(parkingPlace)
    }
    suspend fun decreaseAvailableSpots(parkingPlaceId: Int) {
        parkingDao.decreaseAvailableSpots(parkingPlaceId)
    }
    suspend fun insertBooking(booking: Booking) {
        parkingDao.insertBooking(booking)
    }
    suspend fun deleteAllParkingPlaces() {
        parkingDao.deleteAllParkingPlaces()
    }
    suspend fun deleteAllBookings() {
        parkingDao.deleteAllBookings()
    }
}
