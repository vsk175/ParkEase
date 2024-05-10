package com.example.parkease


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth



class Login : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            LoginScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen() {
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
    val auth = Firebase.auth
    val currentUser by remember { mutableStateOf(auth.currentUser) }
    val context = LocalContext.current
    LoginElements(auth = auth, context = context, launcher)
//    if (currentUser == null) {
//        LoginElements(auth = auth, context = context, navController = navController)
//
//    } else {
//        navController.navigate("Home")
//        auth.signOut()
//
//
//
//
//    }
}
@Composable
fun LoginElements(auth: FirebaseAuth, context: android.content.Context,launcher: ActivityResultLauncher<Intent>) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordErrors by remember { mutableStateOf(listOf<String>()) }
    var showPassword by remember { mutableStateOf(false) }
    Column {
        AppBar()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Login",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            OutlinedTextField(
                value = username, onValueChange = { username = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("User ID") },
                isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(username)
                    .matches() && username.isNotEmpty(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),

                )
            OutlinedTextField(
                value = password, onValueChange = {  password = it
                    passwordErrors = validatePassword(password)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text(text = "Password")},
                leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null) },
                singleLine = true,
                isError = passwordErrors.isNotEmpty(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )
            Row{
                Button(
                    onClick = { signInWithEmailAndPassword(auth, context, username, password, launcher) },
                    colors = ButtonDefaults.buttonColors(Color.Yellow),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Login", color = Color.Black)
                }

                val context = LocalContext.current
                val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("448685550271-hchg1dr263e6q3s7lp40rv9blsflrc38.apps.googleusercontent.com") // Get this from your google-services.json
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, signInOptions)

                val authResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account.idToken!!,context, launcher)
                    } catch (e: ApiException) {
                        // Log or show the specific error
                        Log.e("Google Sign-In Error", "Sign-In Failed: ${e.statusCode}")
                    }
                }


                Button(modifier = Modifier.padding(top = 6.dp)
                    ,onClick = { val signInIntent = googleSignInClient.signInIntent
                        authResultLauncher.launch(signInIntent) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Text(text = " Google Sign-in", color = Color.White)
                }


            }


            Button(
                onClick = { navigateToRegister(context, launcher) },
                shape = RoundedCornerShape(30),
                colors = ButtonDefaults.buttonColors(Color.Yellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                Text(text = "Register", color = Color.Black)

            }
        }
    }
}

private fun navigateToRegister(context: android.content.Context, launcher: ActivityResultLauncher<Intent>)
{
    val intent = Intent(context, Register::class.java)
    launcher.launch(intent)
}

fun firebaseAuthWithGoogle(idToken: String,context: android.content.Context,launcher: ActivityResultLauncher<Intent>) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in succeeded, update UI with the signed-in user's information
                val intent = Intent(context, MainActivity::class.java)
                launcher.launch(intent)


            } else {
                // If sign-in fails, display a message to the user.
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()

            }
        }
}



private fun signInWithEmailAndPassword(
    auth: FirebaseAuth,
    context: android.content.Context,
    email: String,
    password: String,
    launcher: ActivityResultLauncher<Intent>
) {
    if (email.isNotEmpty() && password.isNotEmpty()){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Authentication success, navigate to home screen
                    // Implement navigation logic here, for now, just display a toast
                    Toast.makeText(context, "Authentication successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    launcher.launch(intent)

                } else {
                    // Authentication failed, display error message
                    // Implement error handling UI, for now, just display a toast
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }


    }
    else {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
        navigateToLogin(context, launcher)
    }

}






