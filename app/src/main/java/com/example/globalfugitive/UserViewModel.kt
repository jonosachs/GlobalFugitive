package com.example.globalfugitive

import android.app.Activity
import android.content.ContentValues.TAG
import android.credentials.CredentialManager
import android.credentials.GetCredentialRequest
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistry
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.Credential
import androidx.credentials.CustomCredential


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.api.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
//import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class UserViewModel() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val errorMessage = MutableLiveData<String?>(null)
    var currentUser by mutableStateOf<User?>(null)
        private set



    fun signInWithEmailAndPassword(
        activity: Activity,
        email: String,
        password: String,
        onSignInSuccess: () -> Unit
    ) {

        // Validate input before making the Firebase call
        if (email.isBlank() || password.isBlank()) {
            errorMessage.value = "Email and password cannot be empty."
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    println("userName @ SignInViewModel: ${auth.currentUser}")
                    setUser(auth.currentUser)
                    clearErrorMessage() // Clear the error message
                    onSignInSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    errorMessage.value = generateErrorMessage(task.exception)
                }
            }
    }

    // Function to set the user state in the ViewModel
    fun setUser(user: FirebaseUser?) {
        currentUser = if (user != null) {
            User(
                userId = user.uid,
                email = user.email,
                displayName = user.displayName,
                photoUrl = user.photoUrl?.toString()
            )
        } else {
            null
        }
    }

    fun signInWithGoogle(credential: Credential, onSignInSuccess: () -> Unit) {
        if(credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            signInWithFirebase(googleIdTokenCredential.idToken)
            setUser(auth.currentUser)
            clearErrorMessage()
            onSignInSuccess()
        } else {
            println("Could not sign in with Google")
        }
    }

    fun signInWithFirebase(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            try {
                Firebase.auth.signInWithCredential(credential).await()
            } catch (e: Exception) {
                println("Could not pass Google credentials to Firebase")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        setUser(null)
    }

    fun createUserWithEmailAndPassword(
        activity: Activity,
        email: String,
        password: String,
        onSignInSuccess: () -> Unit
    ) {

        // Validate input before making the Firebase call
        if (email.isBlank() || password.isBlank()) {
            errorMessage.value = "Email and password cannot be empty."
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    setUser(auth.currentUser)
                    clearErrorMessage()
                    onSignInSuccess()
                } else {
                    errorMessage.value = generateErrorMessage(task.exception)
                }
            }
    }

    fun updatePassword(newPassword: String) {
        val user = Firebase.auth.currentUser
        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password updated successfully
                    Log.d("UserProfile", "Password updated successfully.")
                } else {
                    // Handle failure (e.g., re-authentication required)
                    Log.e("UserProfile", "Error updating password.", task.exception)
                }
            }
    }

    fun sendPasswordResetEmail(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Email sent successfully
                    Log.d("UserProfile", "Password reset email sent.")
                } else {
                    // Handle failure
                    Log.e("UserProfile", "Error sending password reset email.", task.exception)
                }
            }
    }


    fun getCurrentUser() {
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid
        }
    }


    // Generate a user-friendly error message based on the exception type
    private fun generateErrorMessage(exception: Exception?): String {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> "Password must be at least 6 characters"
            is FirebaseAuthInvalidCredentialsException ->
                if (exception.errorCode == "ERROR_INVALID_EMAIL") {"Invalid Email address."}
                else if (exception.errorCode == "ERROR_WRONG_PASSWORD") {"Invalid password."}
                else if (exception.errorCode == "ERROR_USER_NOT_FOUND") {"User not found."}
                else if (exception.errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {"Email already registered."}
                else "Invalid credentails, please try again."
            is FirebaseAuthInvalidUserException -> "User does not exist. Please sign up first."
            else -> "Authentication failed. Please try again."
        }
    }

    fun clearErrorMessage() {
        errorMessage.value = null
    }

    fun updateUser() {
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = "Jane Q. User"
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }



}
