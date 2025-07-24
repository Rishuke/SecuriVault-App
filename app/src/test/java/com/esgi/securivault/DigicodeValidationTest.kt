package com.esgi.securivault

import org.junit.Assert.*
import org.junit.Test

class DigicodeValidatorTest {

    private fun isCodeValid(newCode: String, confirmCode: String): Boolean {
        return newCode.isNotEmpty() && newCode == confirmCode
    }

    @Test
    fun `code is valid when matching and not empty`() {
        assertTrue(isCodeValid("1234", "1234"))
    }

    @Test
    fun `code is invalid when new code is empty`() {
        assertFalse(isCodeValid("", "1234"))
    }

    @Test
    fun `code is invalid when confirm code is empty`() {
        assertFalse(isCodeValid("1234", ""))
    }

    @Test
    fun `code is invalid when codes do not match`() {
        assertFalse(isCodeValid("1234", "4321"))
    }

    @Test
    fun `code is invalid when both fields are empty`() {
        assertFalse(isCodeValid("", ""))
    }
}
