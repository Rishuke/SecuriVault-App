package com.esgi.securivault.repository

import com.esgi.securivault.data.dto.ChangeCodeRequest
import com.esgi.securivault.data.dto.SuitcaseDTO
import com.esgi.securivault.networking.services.SuitcaseServices
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuitcaseRepository @Inject constructor(
    private val apiService: SuitcaseServices
) {
    suspend fun changeCode(suitcaseId: String, newCode: String): Result<SuitcaseDTO> {
        return try {
            val response = apiService.changeCode(suitcaseId, ChangeCodeRequest(newCode))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors du changement de code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBuzzerVolume(suitcaseId: String, frequency: Int): Result<SuitcaseDTO> {
        return try {
            val response = apiService.updateBuzzerVolume(suitcaseId, mapOf("buzzer_freq" to frequency))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors de la mise à jour du buzzer"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLedColor(suitcaseId: String, color: String): Result<SuitcaseDTO> {
        return try {
            val response = apiService.updateLedColor(suitcaseId, mapOf("led_color" to color))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors de la mise à jour de la couleur LED"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSuitcaseById(suitcaseId: String): Result<SuitcaseDTO> {
        return try {
            val response = apiService.getSuitcaseById(suitcaseId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors de la récupération de la valise"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}