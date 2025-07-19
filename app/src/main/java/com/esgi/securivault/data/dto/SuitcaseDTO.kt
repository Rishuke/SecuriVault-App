package com.esgi.securivault.data.dto

data class SuitcaseDTO(
    val id: String,
    val name: String,
    val locked: Boolean,
    val on: Boolean,
    val buzzerFreq: Int? = null,
    val ledColor: String? = null,
    val sensitivity: Double? = null
) {
    // Additional methods or properties can be added here if needed
}