package com.example.globalfugitive

import java.util.Date

data class User(
    val userId: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val dateOfBirth: Date?,
    val sex: String?
)

