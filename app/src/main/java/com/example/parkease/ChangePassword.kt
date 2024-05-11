package com.example.parkease

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser

class ChangePassword : ComponentActivity() {
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            intent.getParcelableExtra<FirebaseUser>("User")?.let { user ->
                // Assign it to the local firebaseUser variable
                firebaseUser = user
            }
            Scaffold(
                topBar = {
                    AppBar()
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    PasswordChange(firebaseUser, this@ChangePassword)
                }
            }
        }
    }
}

@Composable
fun PasswordChange(user: FirebaseUser, activity: Activity) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordChangeResult by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {


        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Current Password") }
        )
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") }
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm New Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row{
            Button(onClick = { activity.finish() }) {
                Text("Profile")

            }
            Button(onClick = {
                if (newPassword == confirmPassword) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            passwordChangeResult = if (task.isSuccessful) {
                                "Password Updated Successfully!"
                            } else {
                                "Failed to Update Password."
                            }
                        }
                } else {
                    passwordChangeResult = "Passwords do not match."
                }
            }) {
                Text("Update Password")
            }
            passwordChangeResult?.let {
                Text(it)
            }

        }
    }
}