package com.example.parkease

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ParkingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ParkingRepository = ParkingRepository(application)

    val allParkingPlaces: LiveData<List<ParkingPlace>> = repository.allParkingPlaces.asLiveData()
    val allAvailableParkingPlaces: LiveData<List<ParkingPlace>> = repository.allAvailableParkingPlaces.asLiveData()
    fun getBookingsByUserId(userId: String): Flow<List<Booking>> {
        return repository.getBookingsByUserId(userId)
    }
    fun insert(parkingPlace: ParkingPlace) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(parkingPlace)
        }
    }
    fun decreaseAvailableSpots(parkingPlaceId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.decreaseAvailableSpots(parkingPlaceId)
        }
    }
    fun insertBooking(booking: Booking) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBooking(booking)
        }
    }
    fun deleteAllParkingPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllParkingPlaces()
        }
    }
    fun deleteAllBookings() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllBookings()
        }
    }
}

