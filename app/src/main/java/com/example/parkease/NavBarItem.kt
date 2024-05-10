package com.example.parkease

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
                icon = Icons.Filled.AccountCircle,
                route = Routes.Profile.value
            ),
            NavBarItem(
                label = "History",
                icon = Icons.Filled.Person,
                route = Routes.About.value
            ),
            NavBarItem(
                label = "Booking",
                icon = Icons.Filled.AddCircle,
                route = Routes.Book.value
            )
        )
    }
}

