package com.example.parkease

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

data class NavBarItem (
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    fun navBarItems(): List<NavBarItem> {
        return listOf(
            NavBarItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Routes.Home.value
            ),
            NavBarItem(
                label = "Profile",
                icon = Icons.Filled.Person,
                route = Routes.Profile.value
            ),
            NavBarItem(
                label = "History",
                icon = Icons.Filled.Info,
                route = Routes.History.value
            ),
            NavBarItem(
                label = "Booking",
                icon = Icons.Filled.Create,
                route = Routes.Book.value
            ),
            NavBarItem(
                label = "Map",
                icon = Icons.Filled.Place,
                route = Routes.Map.value
            )
        )
    }
}

