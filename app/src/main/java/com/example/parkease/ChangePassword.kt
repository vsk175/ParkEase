package com.example.parkease

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            PasswordChange(user = firebaseUser)
        }

    }

    @Composable
    fun PasswordChange(user: FirebaseUser) {
        var currentPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordChangeResult by remember { mutableStateOf<String?>(null) }
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
            finish()
        }
    }
}