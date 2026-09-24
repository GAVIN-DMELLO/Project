package com.example.cargrasp.data.model

import android.graphics.Bitmap
import android.net.Uri

/**
 * UI State for the CarGrasp application flow.
 */
sealed interface AnalysisUiState {
    /**
     * Initial idle state on the Home Screen.
     */
    data object Idle : AnalysisUiState

    /**
     * Image has been selected or captured and is ready for preview/analysis.
     */
    data class ImageReady(
        val imageUri: Uri?,
        val bitmap: Bitmap
    ) : AnalysisUiState

    /**
     * Image is currently being processed and analyzed by Gemini AI Vision.
     */
    data class Analyzing(
        val imageUri: Uri?,
        val bitmap: Bitmap,
        val progressStep: String = "Connecting to Gemini Vision AI..."
    ) : AnalysisUiState

    /**
     * Analysis completed successfully with structured result.
     */
    data class Success(
        val imageUri: Uri?,
        val bitmap: Bitmap,
        val result: CarAnalysisResult
    ) : AnalysisUiState

    /**
     * Error encountered during processing, network call, or AI analysis.
     */
    data class Error(
        val imageUri: Uri?,
        val bitmap: Bitmap?,
        val message: String,
        val errorType: ErrorType
    ) : AnalysisUiState
}

/**
 * Classification of errors for user-friendly error UI handling.
 */
enum class ErrorType {
    NO_INTERNET,
    API_KEY_MISSING,
    API_ERROR,
    IMAGE_PROCESSING_FAILED,
    UNKNOWN
}
