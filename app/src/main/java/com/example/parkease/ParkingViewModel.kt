package com.example.parkease

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ParkingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ParkingRepository = ParkingRepository(application)

    val allParkingPlaces: LiveData<List<ParkingPlace>> = repository.allParkingPlaces.asLiveData()

    fun insert(parkingPlace: ParkingPlace) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(parkingPlace)
        }
    }

    fun deleteAllParkingPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllParkingPlaces()
        }
    }
}

