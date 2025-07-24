package com.esgi.securivault.viewmodels

import com.esgi.securivault.data.dto.SuitcaseDTO
import com.esgi.securivault.repository.SuitcaseRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SuitcaseViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: SuitcaseRepository
    private lateinit var viewModel: SuitcaseViewModel

    private val fakeSuitcase = SuitcaseDTO(
        id = "valise002",
        name = "Valise 002",
        locked = false,
        on = true
        // ajoute ici tous les autres champs requis si besoin
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()

        // Mock utile pour initViewModel si nécessaire
        coEvery { repository.getSuitcaseById("valise002") } returns Result.success(fakeSuitcase)

        viewModel = SuitcaseViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `changeCode should update successMessage when API call succeeds`() = runTest {
        coEvery { repository.changeCode("valise002", "1234") } returns Result.success(fakeSuitcase)

        viewModel.setSuitcaseId("valise002")
        viewModel.changeCode("1234")
        advanceUntilIdle()

        assertEquals("Code changé avec succès", viewModel.uiState.value.successMessage)
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `changeCode should update errorMessage when API call fails`() = runTest {
        coEvery { repository.changeCode("valise002", "1234") } returns Result.failure(Exception("Erreur"))

        viewModel.setSuitcaseId("valise002")
        viewModel.changeCode("1234")
        advanceUntilIdle()

        assertEquals("Erreur", viewModel.uiState.value.errorMessage) // ✅
    }
}
