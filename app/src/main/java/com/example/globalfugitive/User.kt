package com.example.globalfugitive

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home

data class User(
    val userId: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
)

