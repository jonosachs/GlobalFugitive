package com.example.globalfugitive

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun UserProfile(
    userViewModel: UserViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user = userViewModel.currentUser

    var isEditing by remember { mutableStateOf(false) }
    var fieldToEdit by remember { mutableStateOf("") }
    var currentField by remember { mutableStateOf("") }

    var showConfirmDelete by remember { mutableStateOf(false) }
    var showConfirmResetEmail by remember { mutableStateOf(false) }

    //Date picker
    val calendar = Calendar.getInstance()
    // Create a DatePicker state and initialize it with the selected date
    var selectedDate by remember { mutableStateOf(calendar.timeInMillis) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
    var dateOfBirth by remember { mutableStateOf(user?.dateOfBirth) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Fields with an "editable" flag
    val fields = listOf(
        Triple("UserId", user?.userId.orEmpty(), false),  // Non-editable
        Triple("Display Name", user?.displayName.orEmpty(), true),  // Editable
        Triple("Email", user?.email.orEmpty(), false),  // Non-editable
        Triple("Date of Birth", dateOfBirth?.let { Date(it) }?.let { formatDate(it) }, true),  // Editable as date picker
        Triple("Gender", user?.gender.orEmpty(), true),  // Editable
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Display user profile picture
        user?.photoUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable { /* Handle profile picture click here */ }
            )
        } ?: run {
            Image(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = "Default Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable { /* Handle profile picture click */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "User Profile", style = MaterialTheme.typography.headlineLarge)

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(fields.size) { index ->
                val (label, value, isEditable) = fields[index]
                // Show normal UserDataField for other fields
                UserDataField(
                    field = label,
                    value = value.orEmpty(),  // Pass the current field value
                    isEditable = isEditable,
                    onEdit = {
                        if (isEditable) {
                            fieldToEdit = label
                            currentField = value.orEmpty()
                            isEditing = true
                        }
                    },
                    onIsDate = { showDatePicker = true },
                    onIsGender = { },
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            userViewModel.clearErrorMessage()
                            showDatePicker = false
                            selectedDate = datePickerState.selectedDateMillis ?: calendar.timeInMillis
                        }
                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {showDatePicker = false}
                    ) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker( state = datePickerState )
            }
        }// end of if


        if (isEditing) {
            EditUserDataField(
                initialFieldValue = currentField,
                onDismiss = { isEditing = false },
                onSave = { updatedField ->
                    val fieldName = when (fieldToEdit) {
                        "Display Name" -> "displayName"
                        "Gender" -> "gender"
                        else -> null
                    }

                    fieldName?.let { field ->
                        user?.userId?.let { userId ->
                            userViewModel.updateUserField(
                                userId = userId,
                                field = field,
                                value = updatedField,
                                onSuccess = { /* Handle success */ },
                                onFailure = { /* Handle failure */ }
                            )
                        }
                    }
                    isEditing = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Reset Button
        Button(onClick = { showConfirmResetEmail = true }) {
            Text("Reset Password")
        }

        if (showConfirmResetEmail) {
            ConfirmDialogue(
                text = "Reset email? A message will be sent to your registered email address.",
                onConfirm = {
                    user?.email?.let { email ->
                        userViewModel.sendPasswordResetEmail(email)
                    }
                    showConfirmResetEmail = false
                },
                onDismiss = { showConfirmResetEmail = false }
            )
        }

        // Delete account
        Button(onClick = { showConfirmDelete = true }) {
            Text("Delete Account")
        }

        if (showConfirmDelete) {
            ConfirmDialogue(
                text = "Warning: Are you sure you want to delete your account?",
                onConfirm = {
                    userViewModel.deleteUser {
                        navController.navigate("SignInScreen") {
                            popUpTo("SignInScreen") { inclusive = true }
                        }
                    }
                    showConfirmDelete = false
                },
                onDismiss = { showConfirmDelete = false }
            )
        }
    }
}


fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}


@Composable
fun UserDataField(
    field: String,
    value: String,
    isEditable: Boolean,
    onEdit: () -> Unit,
    onIsDate: () -> Unit,
    onIsGender: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                Color.LightGray,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$field: $value", modifier = Modifier.weight(1f))

        if (isEditable) {
            if (field == "Date of Birth") {
                IconButton(onClick = onIsDate) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
            } else if (field == "Gender") {
                IconButton(onClick = onIsGender) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
            } else {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }



    }
}


@Composable
fun EditUserDataField(
    initialFieldValue: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var field by remember { mutableStateOf(initialFieldValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Field") },
        confirmButton = {
            Button(
                onClick = {
                    onSave(field)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            TextField(
                value = field,
                onValueChange = { field = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun ConfirmDialogue(
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text)
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("No")
            }
        },
        text = {}
    )
}

