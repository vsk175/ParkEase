package com.example.parkease

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun Home(navController: NavController) {
    val placesViewModel: PlacesViewModel = viewModel()
    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        LocalContext.current)
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
                    .weight(0.4f)
                    .fillMaxWidth()
            ) {
                ParkingPlacesList(parkingViewModel)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
            ) {
                MapCardView(context, fusedLocationProviderClient)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
            ) {
                NearbyPlacesSearch(placesViewModel)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colors.primary,
            titleContentColor = Color.White,
        ),
        title = { Text("ParkEase", fontSize = 30.sp) }
    )
}

@Composable
fun MapCardView(context: Context, fusedLocationProviderClient: FusedLocationProviderClient)
{
    val currentLocation = remember { mutableStateOf<LatLng?>(null) }

    // Fetch current location when the composable is first launched
    LaunchedEffect(Unit) {
        getCurrentLocation(
            context = context,
            fusedLocationProviderClient = fusedLocationProviderClient,
            onGetCurrentLocationSuccess = { location ->
                currentLocation.value = LatLng(location.first, location.second)
                Log.e(
                    "MapCardView",
                    "Location received - Latitude: ${location.first}, Longitude: ${location.second}"
                )
            },
            onGetCurrentLocationFailed = { exception ->
                Log.e("MapCardView", "Error getting location: $exception")
            }
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        // If current location is available, use it; otherwise, use a default location
        val location = currentLocation.value ?: LatLng(-37.8136, 144.9631) // Default to Melbourne city center
        position = CameraPosition.fromLatLngZoom(location, 10f)
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(10.dp)
            .size(width = 380.dp, height = 200.dp)
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
                    title = "Marker at Current Location"
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: Context,
    fusedLocationProviderClient: FusedLocationProviderClient,
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception) -> Unit,
    priority: Boolean = true
) {
    // Determine the accuracy priority based on the 'priority' parameter
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    // Check if location permissions are granted
    if (areLocationPermissionsGranted(context)) {
        // Retrieve the current location asynchronously
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                // If location is not null, invoke the success callback with latitude and longitude
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }
        }.addOnFailureListener { exception ->
            // If an error occurs, invoke the failure callback with the exception
            onGetCurrentLocationFailed(exception)
        }
    }
}

private fun areLocationPermissionsGranted(context: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}

// Use this to function to add or delete parking places
fun addParkingPlaces(viewModel: ParkingViewModel) {
    viewModel.deleteAllParkingPlaces()
    val parkingPlaces = listOf(
        ParkingPlace(name = "Bunnings Notting Hill Parking", address = "Clayton VIC 3168", latitude = -37.90038028805474, longitude = 145.12747260953225, availableSpots = 10, totalSpots = 10),
        ParkingPlace(name = "Malvern Central Parking", address = "110-122 Wattletree Rd, Malvern VIC 3144", latitude = -37.86207385848278, longitude = 145.027662361082, availableSpots = 10, totalSpots = 10),
        ParkingPlace(name = "Caulfield Plaza Parking Area", address = "19 Sir John Monash Dr, Caulfield East VIC 3145", latitude = -37.87668190529675, longitude = 145.04253101138096, availableSpots = 10, totalSpots = 10),
        ParkingPlace(name = "2 Clynden Ave Parking", address = "2 Clynden Ave, Hawthorn East VIC 3123", latitude = -37.867550210079294, longitude = 145.06235679884233, availableSpots = 10, totalSpots = 10),
        ParkingPlace(name = "Ace Parking", address = "8A Prospect St, Box Hill VIC 3128", latitude = -37.817333958084404, longitude = 145.11961322042757, availableSpots = 10, totalSpots = 10)
    )

    // Insert each parking place through the ViewModel
    parkingPlaces.forEach { parkingPlace ->
        viewModel.insert(parkingPlace)
    }
}

@Composable
fun NearbyPlacesSearch(viewModel: PlacesViewModel) {
    var locationName by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf(500) }
    val placesResponse by viewModel.placesResponse
    val errorMessage by viewModel.errorMessage

    // Get the current context to use the Geocoder class
    val context = LocalContext.current

    Column(modifier = Modifier
        .padding(8.dp)
        .verticalScroll(rememberScrollState())) {
        Text(
            text = "Find Train Stations nearest to our Parking",
            style = MaterialTheme.typography.h6,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp, top = 8.dp) // Add space below the heading and some horizontal padding
        )
        Row(modifier = Modifier.fillMaxWidth()){
            // Place Name Input
            OutlinedTextField(
                value = locationName,
                onValueChange = { locationName = it ?: "" },
                label = { Text("Enter parking place") },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp)
                    .height(60.dp)
            )

            // Radius Input
            OutlinedTextField(
                value = radius.toString(),
                onValueChange = { radius = it.toIntOrNull() ?: 500 },
                label = { Text("Radius (meters)") },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp)
                    .height(60.dp)
            )
        }
        //MapCardView()
        // Search Button
        Column(
            modifier = Modifier
                .fillMaxWidth() // Make sure the parent fills the full width
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center align child horizontally
        ) {
            // Your Button here
            Button(
                onClick = { viewModel.fetchNearbyPlacesByName(context, locationName, radius) },
                modifier = Modifier
                    .width(100.dp)
                    .height(35.dp)
            ) {
                Text("Search")
            }
        }

        // Display Error Message if Any
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Display Results in LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .padding(horizontal = 16.dp, vertical = 2.dp) // Add padding around the list
        ) {
            items(placesResponse.results) { result ->
                val distance = viewModel.calculateDistanceToPlace(
                    result.geometry.location.lat,
                    result.geometry.location.lng
                )

                // Each place information is displayed in an elevated card for better visual clarity
                ElevatedCard(
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp) // Add space between cards
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp) // Padding inside the card for each item
                    ) {
                        // Row with station name and suburb
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp), // Space between rows
                            horizontalArrangement = Arrangement.SpaceBetween // Evenly distribute elements
                        ) {
                            Text(
                                text = "Station: ${result.name}",
                                style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Suburb: ${result.vicinity}",
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                            )
                        }

                        // Distance text in a separate row
                        Text(
                            text = "Distance: ${"%.2f".format((distance ?: 0f) / 1000)} km",
                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun ParkingPlacesList(viewModel: ParkingViewModel) {
    // Collect the LiveData into a state that Compose can watch
    val parkingPlaces by viewModel.allParkingPlaces.observeAsState(initial = emptyList())
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Explore our Parking Places",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp, top = 8.dp) // Add space below the heading and some horizontal padding
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(parkingPlaces) { parkingPlace ->
                    ParkingPlaceItem(parkingPlace)
                }
        }
    }
}

@Composable
fun ParkingPlaceItem(parkingPlace: ParkingPlace) {
    var expanded by remember { mutableStateOf(false) }  // Remember expanded state
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 8.dp) // Add padding around the item
    ) {
        // Elevated card to display parking place details
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = !expanded })  // Toggle expanded state
                .animateContentSize() // Smoothly animate content size change
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)  // Increase padding for better touch area
            ) {
                // Row with parking place name and address
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),  // Space between rows
                ) {
                    Text(
                        text = "Place: ${parkingPlace.name}",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Address: ${parkingPlace.address}",
                        style = MaterialTheme.typography.body1
                    )
                }

                // Optionally expand to show more details
                if (expanded) {
                    Divider(color = MaterialTheme.colors.primary)
                    Text(
                        text = "Available Spots: ${parkingPlace.availableSpots}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Total Spots: ${parkingPlace.totalSpots}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}
//the below code will get the current userID to the variable
//val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

