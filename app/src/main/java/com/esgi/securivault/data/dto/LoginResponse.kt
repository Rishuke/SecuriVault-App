package com.esgi.securivault.data.dto

data class LoginResponse(
    val idToken: String,
    val refreshToken: String,
)