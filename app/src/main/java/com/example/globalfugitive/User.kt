package com.example.globalfugitive

import com.google.firebase.Timestamp

data class User(
    val userId: String,
    val email: String?,
    var displayName: String?,
    val photoUrl: String?,
    val dateOfBirth: Long?,
    val gender: String?
)

