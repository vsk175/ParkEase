package com.example.parkease

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun Map(navController: NavController) {
    val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            LocalContext.current
        )
    val parkingViewModel: ParkingViewModel = viewModel()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            AppBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                MapScreen(context, fusedLocationProviderClient, parkingViewModel)
            }
        }
    }
}

@Composable
fun MapScreen(context: Context, fusedLocationProviderClient: FusedLocationProviderClient, parkingViewModel: ParkingViewModel) {

    val currentLocation = remember { mutableStateOf<LatLng?>(null) }

    // Fetch current location when the composable is first launched
    LaunchedEffect(Unit) {
        getCurrentLocation(
            context = context,
            fusedLocationProviderClient = fusedLocationProviderClient,
            onGetCurrentLocationSuccess = { location ->
                currentLocation.value = LatLng(location.first, location.second)
            },
            onGetCurrentLocationFailed = { exception ->
                Log.e("MapCardView", "Error getting location: $exception")
            }
        )
    }
    val cameraPositionState = rememberCameraPositionState {
        // If current location is available, use it; otherwise, use a default location
        val location = currentLocation.value ?: LatLng(-37.9142, 145.1347) // Default to Melbourne city center
        position = CameraPosition.fromLatLngZoom(location, 10f)
    }
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        )
        {
            GoogleMap(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                cameraPositionState = cameraPositionState
            )
            {
                currentLocation.value?.let { location ->
                    AdvancedMarker(
                        state = MarkerState(position = location),
                        title = "Your Location"
                    )
                }
                val parkingPlacesState = parkingViewModel.allParkingPlaces.observeAsState()
                val parkingPlaces = parkingPlacesState.value ?: emptyList()
                parkingPlaces.forEach { parkingPlace ->
                    AdvancedMarker(
                        state = MarkerState(position = LatLng(parkingPlace.latitude, parkingPlace.longitude)),
                        title = parkingPlace.name
                    )
                }


            }
        }
}


