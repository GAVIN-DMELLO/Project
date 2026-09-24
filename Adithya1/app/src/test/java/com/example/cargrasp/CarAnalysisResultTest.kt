package com.example.cargrasp

import com.example.cargrasp.data.model.CarAnalysisResult
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CarAnalysisResultTest {

    private val gson = Gson()

    @Test
    fun `test valid car JSON deserialization`() {
        val json = """
            {
              "carDetected": true,
              "make": "BMW",
              "model": "3 Series",
              "colour": "Metallic Blue",
              "confidence": "High",
              "bodyType": "Sedan",
              "estimatedYearRange": "2020-2023",
              "notes": "Distinctive double-kidney grille and angel-eye LED daytime running lights."
            }
        """.trimIndent()

        val result = gson.fromJson(json, CarAnalysisResult::class.java)

        assertTrue(result.carDetected)
        assertEquals("BMW", result.make)
        assertEquals("3 Series", result.model)
        assertEquals("Metallic Blue", result.colour)
        assertEquals("High", result.confidence)
        assertEquals("Sedan", result.bodyType)
        assertEquals("BMW 3 Series", result.getFullTitle())
        assertTrue(result.isRecognized())
    }

    @Test
    fun `test non-car image JSON deserialization`() {
        val json = """
            {
              "carDetected": false,
              "make": "Not Detected",
              "model": "Not Detected",
              "colour": "Not Detected",
              "confidence": "Uncertain",
              "bodyType": "Unknown",
              "estimatedYearRange": "Unknown",
              "notes": "No automobile was recognized in this photo."
            }
        """.trimIndent()

        val result = gson.fromJson(json, CarAnalysisResult::class.java)

        assertFalse(result.carDetected)
        assertEquals("Car not detected", result.getFullTitle())
        assertFalse(result.isRecognized())
    }
}
