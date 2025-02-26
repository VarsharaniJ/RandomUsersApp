package com.example.randomuserapp.domain.model

data class User(
    val id: String, // Unique identifier
    val firstName: String,
    val lastName: String,
    val address: String,
    val profilePicture: String
)