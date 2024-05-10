package com.example.parkease

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.*
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth


class BookingActivity : ComponentActivity() {
    private lateinit var viewModel: ParkingViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ParkingViewModel(application)
        setContent {
            val parkingId = intent.getStringExtra("parkingId")
            val parkingName = intent.getStringExtra("parkingName")
            val parkingAddress = intent.getStringExtra("parkingAddress")

            Column {
                ParkingPlaceInfo(parkingName ?: "", parkingAddress ?: "", parkingId ?: "")
                Spacer(modifier = Modifier.height(16.dp))
                if (parkingId != null) {
                    BookingForm(onBookClick = { booking ->
                        viewModel.insertBooking(booking)
                    }, parkingId.toInt())
                }
            }
        }
    }

@Composable
fun ParkingPlaceInfo(name: String, address: String, id: String) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Parking Place Name",
            color = Color.DarkGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Address",
            color = Color.DarkGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = address,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun BookingForm(onBookClick: (Booking) -> Unit, parkingId: Int) {
        val (bookingDate, setBookingDate) = remember { mutableStateOf(Date()) }
        val (bookingTime, setBookingTime) = remember { mutableStateOf(Date()) }
        val (durationHours, setDurationHours) = remember { mutableStateOf(TextFieldValue()) }

        // Jetpack Compose doesn't have a DatePicker yet, so we use AndroidView to wrap it
        val context = LocalContext.current

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display the picked booking date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Selected Booking Date:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatDate(bookingDate),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(
                    onClick = {
                        val currentDate = Calendar.getInstance().time
                        showDatePickerDialog(
                            context,
                            initialDate = currentDate,
                            minDate = currentDate,
                            onDateSelected = setBookingDate
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Booking Date"
                    )
                }
            }

            // Display the picked booking time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Selected Booking Time:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatDate(bookingTime, "HH:mm"), // Format time to show only hours and minutes
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(
                    onClick = {
                        showTimePickerDialog(context, bookingTime) { selectedTime ->
                            setBookingTime(selectedTime)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Booking Time"
                    )
                }
            }

            // Field for the duration of booking in hours
            TextField(
                value = durationHours,
                onValueChange = { setDurationHours(it) },
                label = { Text("Duration (hours)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            // Button to submit booking
            Button(
                onClick = {
                    val durationHoursValue = durationHours.text.toIntOrNull() ?: 0
                    val booking = Booking(
                            placeId = parkingId, // Convert parkingId to Int
                            userId = userId,
                            bookingDate = bookingDate,
                            bookingTime = bookingTime,
                            durationHours = durationHoursValue, // Convert text to Int or default to 0
                            status = BookingStatus.ACTIVE
                        )
                    onBookClick(booking)
                },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text("Book")
            }
        }
    }

// Function to format Date to string with specified pattern
private fun formatDate(date: Date, pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(date)
}

// Function to show date picker dialog
private fun showDatePickerDialog(
    context: Context,
    initialDate: Date,
    minDate: Date,
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = initialDate

    // Set minimum date
    val minCalendar = Calendar.getInstance()
    minCalendar.time = minDate

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            onDateSelected(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    // Set minimum date for the date picker
    datePicker.datePicker.minDate = minCalendar.timeInMillis
    datePicker.show()
}
}

// Function to format Date to string with date only
fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(date)
}

fun showTimePickerDialog(
    context: Context,
    initialTime: Date,
    onTimeSelected: (Date) -> Unit
) {


    val currentTime = Calendar.getInstance()
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
                set(Calendar.SECOND, 0) // Optional: Setting seconds to zero
            }.time
            onTimeSelected(selectedTime)
        },
        hour,
        0, // Setting minutes to zero
        true
    )

    timePicker.show()
}




