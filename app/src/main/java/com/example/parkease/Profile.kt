package com.example.parkease

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Profile : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Profile(navController = rememberNavController())
        }
    }
}

@Composable
fun Profile(navController: NavController) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result here
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle success
        } else {
            // Handle failure or other cases
        }
    }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("User Profile", modifier = Modifier.padding(8.dp))

        user?.email?.let { email ->
            Text(text = "Email: $email",modifier = Modifier.padding(8.dp))
        }
        Button(onClick = {   }
            ,colors = ButtonDefaults.buttonColors(Color.Yellow),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp))
        {
            Text(text ="Change Password", color = Color.Black)

        }
        Button(onClick = {auth.signOut()
                navigateToLogin(context, launcher)}
            ,colors = ButtonDefaults.buttonColors(Color.Yellow),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp))
        {
            Text(text ="Sign-Out", color = Color.Black)

        }


    }
}


@Composable
fun PasswordChange(user: FirebaseUser?,context: android.content.Context,launcher: ActivityResultLauncher<Intent>){
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
    Button(onClick = { val intent = Intent(context, Profile::class.java)
        launcher.launch(intent) }, ) {
        Text(text = "Back")

    }
    Button(onClick = {
        if (newPassword == confirmPassword) {
            user?.updatePassword(newPassword)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        passwordChangeResult = "Password Updated Successfully!"
                    } else {
                        passwordChangeResult = "Failed to Update Password."
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

