package com.esgi.securivault.data.dto

data class LoginResponse(
    val idtoken: String,
    val refreshToken: String,
)