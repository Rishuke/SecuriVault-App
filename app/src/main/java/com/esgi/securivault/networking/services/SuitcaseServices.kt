package com.esgi.securivault.networking.services

import com.esgi.securivault.data.dto.ChangeCodeRequest
import com.esgi.securivault.data.dto.SuitcaseDTO
import retrofit2.Response
import retrofit2.http.*

interface SuitcaseServices {
    @PUT("suitcases/{id}/change-code")
    suspend fun changeCode(
        @Path("id") suitcaseId: String,
        @Body request: ChangeCodeRequest
    ): Response<SuitcaseDTO>

    @PUT("suitcases/{id}/buzzer")
    suspend fun updateBuzzerVolume(
        @Path("id") suitcaseId: String,
        @Body request: Map<String, Int>
    ): Response<SuitcaseDTO>

    @PUT("suitcases/{id}/led-color")
    suspend fun updateLedColor(
        @Path("id") suitcaseId: String,
        @Body request: Map<String, String>
    ): Response<SuitcaseDTO>

    @GET("suitcases/{id}")
    suspend fun getSuitcaseById(@Path("id") suitcaseId: String): Response<SuitcaseDTO>
}
