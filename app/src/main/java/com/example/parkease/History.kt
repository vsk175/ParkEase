package com.example.parkease

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun History(navController: NavController) {
    val viewModel: ParkingViewModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val bookingHistory by viewModel.getBookingsByUserId(userId).collectAsState(initial = emptyList())
    val parkingPlaces by viewModel.allAvailableParkingPlaces.observeAsState(initial = emptyList())

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
            Text(
                text = "Your Parking History",
                style = MaterialTheme.typography.h5,
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp, top = 16.dp) // Add space below the heading and some horizontal padding
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(bookingHistory) { booking ->
                    val parkingPlace = parkingPlaces.find { it.id == booking.placeId }
                    parkingPlace?.let {
                        HistoryItem(booking = booking, parkingPlace = it)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(booking: Booking, parkingPlace: ParkingPlace) {

    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Booking ID: ${booking.id}",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Date: ${dateFormatter.format(booking.bookingDate)}",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Time: ${timeFormatter.format(booking.bookingTime)}",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Duration: ${booking.durationHours} hours",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Parking Name: ${parkingPlace.name}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Parking Address: ${parkingPlace.address}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}