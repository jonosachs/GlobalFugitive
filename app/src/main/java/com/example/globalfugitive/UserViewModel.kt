package com.example.globalfugitive

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
//import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase


class UserViewModel() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val errorMessage = MutableLiveData<String?>(null)
    var currentUser by mutableStateOf<User?>(null)
        private set

    fun signInWithEmailAndPassword(
        activity: Activity,
        email: String,
        password: String,
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    println("userName @ SignInViewModel: ${auth.currentUser}")
                    setUser(auth.currentUser)
                    errorMessage.value = null // Clear the error message
                } else {
                    // If sign in fails, display a message to the user.
                    val errorMsg = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Invalid credentials. Please check your email and password."
                        is FirebaseAuthInvalidUserException -> "User does not exist. Please sign up first."
                        else -> "Authentication failed. Please try again."
                    }
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    errorMessage.value = errorMsg // Call the onError callback with the error message
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

    fun signOut() {
        auth.signOut()
        setUser(null)
    }

    fun createUserWithEmailAndPassword(activity: Activity, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
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




}
