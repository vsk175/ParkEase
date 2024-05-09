package com.example.parkease

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parkease.ui.theme.ParkEaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Register : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Registration()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registration(){
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
    var firstname by remember { mutableStateOf("")}
    var lastname by remember { mutableStateOf("")}
    var useremail by remember { mutableStateOf("")}
    val pattern = remember { Regex("[a-zA-Z\\s]*") }
    val passwordRegex = Regex("^(?=.*[\\W])(?=\\S+$).{8,}$")
    var phoneNumber by remember { mutableStateOf("")}
    var useraddress by remember { mutableStateOf("") }
    val states = listOf("VIC", "QLD", "NSW", "SA", "TAS", "WA", "ACT", "NT")
    var password by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false)}
    var selectedState by remember { mutableStateOf(states[0]) }
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var showPassword by remember { mutableStateOf(false) }

Column {
    AppBar()

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceEvenly) {
        Text("Registration", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        OutlinedTextField(
            value = firstname, onValueChange = {if(it.length <= 50 &&  it.matches(pattern)) firstname = it },
            label = { Text("First Name")},
            leadingIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = null)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,

        )
        OutlinedTextField(value = lastname, onValueChange = {if(it.length <= 50 &&  it.matches(pattern)) lastname = it},
            label = { Text("Last Name")},
            leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            singleLine = true,
        )
        OutlinedTextField(value = useremail, onValueChange = {useremail = it},
            leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(useremail).matches() && useremail.isNotEmpty(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),

        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {if(it.length <= 10 ) phoneNumber = it },
            label = { Text("Phone Number")},
            leadingIcon = {Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),


            isError = phoneNumber.isNotEmpty() && phoneNumber.length != 10,
        )

        OutlinedTextField(value = useraddress, onValueChange = {useraddress = it},
            label = { Text("Address")},
            leadingIcon = {Icon(imageVector = Icons.Outlined.Home, contentDescription = null)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            singleLine = true
            )
        OutlinedTextField(
            value = password, onValueChange = {  password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text(text = "Password")},
            leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null) },
            singleLine = true,
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
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = Modifier.padding(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .focusProperties {
                        canFocus = false
                    }
                    .padding(bottom = 8.dp),
                readOnly = true,
                value = selectedState,
                onValueChange = {},
                label = { Text("State") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            )
            {
                states.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedState = selectionOption
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }


            }
        }
        val date = remember { mutableStateOf(LocalDate.now())}
        val isOpen = remember { mutableStateOf(false)}
        Row(verticalAlignment = Alignment.CenterVertically) {

            OutlinedTextField(
                readOnly = true,
                value = date.value.format(DateTimeFormatter.ISO_DATE),
                label = { Text("Date of Birth") },
                onValueChange = {},
                modifier = Modifier.padding(horizontal = 8.dp)
                )



                IconButton(
                    onClick = { isOpen.value = true } // show de dialog
                ) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
                }

        }

        if (isOpen.value) {
            CustomDatePickerDialog(
                onAccept = {
                    isOpen.value = false // close dialog
                },
                onCancel = {
                    isOpen.value = false //close dialog
                }
            )
        }
        Row {


            Button(onClick = {navigateToLogin(context, launcher)}
                ,colors = ButtonDefaults.buttonColors(Color.Yellow),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp))
            {
                Text(text ="Back to Login", color = Color.Black)

            }
            Button(
                onClick = {registerUser(useremail, password, firebaseAuth, context)
                    saveUserInfo(firstname,lastname,useremail,phoneNumber,useraddress,db)}, colors = ButtonDefaults.buttonColors(Color.Yellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Text(text = "Register", color = Color.Black)
            }
        }

    }

}
}

private fun navigateToLogin(context: android.content.Context, launcher: ActivityResultLauncher<Intent>)
{
    val intent = Intent(context, Login::class.java)
    launcher.launch(intent)
}


private fun saveUserInfo(firstname:String, lastname:String,email:String,phonenumber:String,address:String, db: FirebaseFirestore) {
    val user = RegInfo(firstname, lastname, email, phonenumber, address)
    db.collection("users")
        .add(user)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error adding document", e)
        }
}

fun registerUser(email: String, password: String, auth: FirebaseAuth, context: Context) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registration successful!: ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    } else {
        Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = state)
    }
}

fun isValidEmail(email: String): Boolean {
    // Basic email validation, you can implement more sophisticated validation
    val emailRegex = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
    return emailRegex.matches(email)
}



@Preview
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun registrationpreview(){
    ParkEaseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Registration()
        }
    }
}







