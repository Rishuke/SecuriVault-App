package com.esgi.securivault.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.securivault.data.dto.SuitcaseDTO
import com.esgi.securivault.repository.SuitcaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = false,
    val suitcase: SuitcaseDTO? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class SuitcaseViewModel @Inject constructor(
    private val repository: SuitcaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _suitcaseId = MutableStateFlow("")
    val suitcaseId: StateFlow<String> = _suitcaseId.asStateFlow()

    fun setSuitcaseId(id: String) {
        _suitcaseId.value = id
        loadSuitcase()
    }

    private fun loadSuitcase() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            repository.getSuitcaseById(_suitcaseId.value)
                .onSuccess { suitcase ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        suitcase = suitcase,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun changeCode(newCode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            repository.changeCode(_suitcaseId.value, newCode)
                .onSuccess { suitcase ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        suitcase = suitcase,
                        successMessage = "Code changé avec succès",
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun updateBuzzerFrequency(frequency: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            repository.updateBuzzerVolume(_suitcaseId.value, frequency)
                .onSuccess { suitcase ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        suitcase = suitcase,
                        successMessage = "Fréquence du buzzer mise à jour",
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun updateLedColor(color: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            repository.updateLedColor(_suitcaseId.value, color)
                .onSuccess { suitcase ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        suitcase = suitcase,
                        successMessage = "Couleur LED mise à jour",
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}