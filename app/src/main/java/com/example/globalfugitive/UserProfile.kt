package com.example.globalfugitive

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources.Theme
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


@Composable
fun UserProfile(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user = userViewModel.currentUser

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display user profile picture
        user?.photoUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable { /* Handle profile picture click here, like opening an image picker */ }
            )
        } ?: run {
            // Default placeholder if no profile picture exists
            Image(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = "Default Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable { /* Handle profile picture click here */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display user name
        Text(
            text = "Name: ${user?.displayName ?: "N/A"}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display user email
        Text(
            text = "Email: ${user?.email ?: "N/A"}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Reset Button
        Button(onClick = {
            user?.email?.let { email ->
                userViewModel.sendPasswordResetEmail(email, context)
            }
        }) {
            Text("Reset Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upload Photo Button
        Button(onClick = {
            // Implement image picker and photo upload functionality here
            pickAndUploadImage(context, userViewModel)
        }) {
            Text("Upload New Photo")
        }
    }
}

// Function to trigger password reset email
fun UserViewModel.sendPasswordResetEmail(email: String, context: Context) {
    Firebase.auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password reset email sent to $email", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Failed to send reset email", Toast.LENGTH_LONG).show()
            }
        }
}

// Function to pick and upload image
fun pickAndUploadImage(context: Context, userViewModel: UserViewModel) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    (context as? Activity)?.startActivityForResult(intent, 100)

    // Handle the result in the activity's onActivityResult:
    // userViewModel.uploadImage(uri) with the selected image URI
}

// Function to handle image upload
fun UserViewModel.uploadImage(uri: Uri) {
    val user = Firebase.auth.currentUser
    val storageReference = Firebase.storage.reference.child("profileImages/${user?.uid}")
    storageReference.putFile(uri)
        .addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                user?.updateProfile(
                    UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUri)
                    .build())
                    ?.addOnCompleteListener { profileUpdateTask ->
                        if (profileUpdateTask.isSuccessful) {
                            // Update photo URL in ViewModel
                            setUser(user)
                        }
                    }
            }
        }
        .addOnFailureListener { exception ->
            Log.e("UserProfile", "Failed to upload image: ${exception.message}")
        }
}
