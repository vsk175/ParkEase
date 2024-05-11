package com.example.parkease

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun Book(navController: NavController) {
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
                BookingParkingList(parkingViewModel, navController)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Click on a parking place to book your spot now",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Magenta,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp, top = 16.dp) // Add space below the heading and some horizontal padding
                )

            }
        }
    }
}

@Composable
fun BookingParkingList(viewModel: ParkingViewModel, navController: NavController) {
    val context = LocalContext.current
    // Collect the LiveData into a state that Compose can watch
    val parkingPlaces by viewModel.allAvailableParkingPlaces.observeAsState(initial = emptyList())
    // State to store the selected parking place

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = " Available Parking Places ",
            style = MaterialTheme.typography.h5,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp, top = 16.dp) // Add space below the heading and some horizontal padding
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(parkingPlaces) { parkingPlace ->
                PlaceItem(parkingPlace, context, navController)
            }
        }
    }
}

@Composable
fun PlaceItem(parkingPlace: ParkingPlace, context: android.content.Context, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp) // Add padding around the item
            .clickable {
                val intent = Intent(context, BookingActivity::class.java).apply {
                    putExtra("parkingId", parkingPlace.id.toString())
                    putExtra("parkingName", parkingPlace.name)
                    putExtra("parkingAddress", parkingPlace.address)
                    putExtra("navController", navController.toString())
                }
                context.startActivity(intent)
            }
    ) {
        // Elevated card to display parking place details
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)  // Increase padding for better touch area
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







