package com.example.parkease

import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.checkerframework.common.value.qual.EnsuresMinLenIf
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
    var emailErrors by remember { mutableStateOf(listOf<String>()) }
    val pattern = remember { Regex("[a-zA-Z\\s]*") }
    var phoneNumber by remember { mutableStateOf("")}
    var useraddress by remember { mutableStateOf("") }
    val states = listOf("VIC", "QLD", "NSW", "SA", "TAS", "WA", "ACT", "NT")
    var password by remember { mutableStateOf("") }
    var passwordErrors by remember { mutableStateOf(listOf<String>()) }
    var isExpanded by remember { mutableStateOf(false)}
    var selectedState by remember { mutableStateOf(states[0]) }
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()
    var showPassword by remember { mutableStateOf(false) }

Column {
    AppBar()

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp).verticalScroll(scrollState), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceEvenly) {
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
        OutlinedTextField(value = useremail, onValueChange = {useremail = it
                                                             emailErrors = validateEmail(useremail)
        },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(useremail).matches() && useremail.isEmpty(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),

        )
        emailErrors.forEach { error ->
           Text(text = error,color = Color.Red,
               modifier = Modifier.padding(start = 16.dp))
        }


        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number")},
            leadingIcon = {Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),


            isError = phoneNumber.length != 10,
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
            value = password, onValueChange = { password = it
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
        passwordErrors.forEach { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp) // Adjust padding as needed
            )
        }

        Text("* Password must be Greater than 7 characters and must contain")
        Text("• A UpperCase character")
        Text("• A LowerCase character")
        Text("• A Number")
        Text("• A Special character")
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


            Button(onClick = {navigateToLogin(context, launcher)},
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp))
            {
                Text(text ="Back to Login")
            }
            Button(
                onClick = { if (emailErrors.isEmpty() && passwordErrors.isEmpty()){ registerUser(useremail, password, firebaseAuth, context, launcher)
                    saveUserInfo(firstname,lastname,useremail,phoneNumber,useraddress,db)}
                          else{
                    Toast.makeText(context, "Check Email and password", Toast.LENGTH_SHORT).show()
                          }},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Text(text = "Register")
            }
        }

    }

}
}

fun navigateToLogin(context: android.content.Context, launcher: ActivityResultLauncher<Intent>)
{
    val intent = Intent(context, Login::class.java)
    launcher.launch(intent)
}


private fun saveUserInfo(firstname:String, lastname:String, email:String, phonenumber:String,address:String, db: FirebaseFirestore) {
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

fun registerUser(email: String, password: String, auth: FirebaseAuth, context: Context,launcher: ActivityResultLauncher<Intent>) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registration successful!: ", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    launcher.launch(intent)
                } else {
                    Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    } else if (email.isEmpty()) {
        Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
    }
    else if (password.isEmpty()) {
        Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
    }
    else {
        Toast.makeText(context, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit,
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


fun validatePassword(password: String): List<String> {
    val errors = mutableListOf<String>()
    if (password.isEmpty()){
        errors.add("Password must not be empty")
    }
    if (password.length < 8) {
        errors.add("Password must be at least 8 characters long.")
    }
    if (!password.any { it.isDigit() }) {
        errors.add("Password must contain at least one digit.")
    }
    if (!password.any { it.isUpperCase() }) {
        errors.add("Password must contain at least one uppercase letter.")
    }
    if (!password.any { it.isLowerCase() }) {
        errors.add("Password must contain at least one lowercase letter.")
    }
    if (!password.any { it in "!@#$%^&*()-+=" }) {
        errors.add("Password must contain at least one special character (!@#$%^&*()-+=).")
    }
    if (password.any {it in " "}){
        errors.add("Password should not have any whitespaces")

    }
    return errors
}

fun validateEmail(email: String): List<String> {
    val errors = mutableListOf<String>()
    if (email.isEmpty()){
        errors.add("Email should not be empty")
    }
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        errors.add("Not a valid email address")
    }
    return errors
}










