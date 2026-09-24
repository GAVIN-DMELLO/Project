package com.example.cargrasp.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cargrasp.data.model.AnalysisUiState
import com.example.cargrasp.data.model.CarAnalysisResult
import com.example.cargrasp.data.model.ErrorType
import com.example.cargrasp.data.repository.ApiKeyStorage
import com.example.cargrasp.data.repository.GeminiCarRepository
import com.example.cargrasp.utils.BitmapUtils
import com.example.cargrasp.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarGraspViewModel(application: Application) : AndroidViewModel(application) {

    private val apiKeyStorage = ApiKeyStorage(application.applicationContext)
    private val repository = GeminiCarRepository(apiKeyStorage)

    private val _uiState = MutableStateFlow<AnalysisUiState>(AnalysisUiState.Idle)
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    // Holds the active bitmap and uri for analysis and retries
    private var currentBitmap: Bitmap? = null
    private var currentUri: Uri? = null

    /**
     * Called when an image is chosen from the Photo Picker gallery.
     */
    fun onImageSelectedFromGallery(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = AnalysisUiState.Analyzing(
                imageUri = uri,
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
                progressStep = "Loading image from gallery..."
            )

            val bitmap = BitmapUtils.decodeBitmapFromUri(getApplication(), uri)
            if (bitmap != null) {
                currentBitmap = bitmap
                currentUri = uri
                _uiState.value = AnalysisUiState.ImageReady(imageUri = uri, bitmap = bitmap)
            } else {
                _uiState.value = AnalysisUiState.Error(
                    imageUri = uri,
                    bitmap = null,
                    message = "Could not load image. Please select a valid JPEG or PNG file.",
                    errorType = ErrorType.IMAGE_PROCESSING_FAILED
                )
            }
        }
    }

    /**
     * Called when a photo is captured via CameraX.
     */
    fun onPhotoCaptured(bitmap: Bitmap) {
        viewModelScope.launch {
            val optimizedBitmap = BitmapUtils.optimizeBitmap(bitmap)
            val uri = BitmapUtils.saveBitmapToCache(getApplication(), optimizedBitmap)
            currentBitmap = optimizedBitmap
            currentUri = uri
            _uiState.value = AnalysisUiState.ImageReady(imageUri = uri, bitmap = optimizedBitmap)
        }
    }

    /**
     * Sends the currently loaded image to Gemini AI Vision for vehicle identification.
     */
    fun analyzeCar() {
        val bitmap = currentBitmap
        val uri = currentUri

        if (bitmap == null) {
            _uiState.value = AnalysisUiState.Error(
                imageUri = uri,
                bitmap = null,
                message = "No image selected. Please take a photo or select one from the gallery.",
                errorType = ErrorType.IMAGE_PROCESSING_FAILED
            )
            return
        }

        viewModelScope.launch {
            // 1. Verify Internet Connection
            if (!NetworkUtils.isNetworkAvailable(getApplication())) {
                _uiState.value = AnalysisUiState.Error(
                    imageUri = uri,
                    bitmap = bitmap,
                    message = "No internet connection detected. Please connect to Wi-Fi or Mobile Data to analyze the car with Gemini AI.",
                    errorType = ErrorType.NO_INTERNET
                )
                return@launch
            }

            // 2. Check API Key
            if (!apiKeyStorage.isApiKeyConfigured()) {
                _uiState.value = AnalysisUiState.Error(
                    imageUri = uri,
                    bitmap = bitmap,
                    message = "Gemini API key is missing. Please configure your key in local.properties or via the API Key settings button.",
                    errorType = ErrorType.API_KEY_MISSING
                )
                return@launch
            }

            // 3. Set Analyzing State with animated status messages
            _uiState.value = AnalysisUiState.Analyzing(
                imageUri = uri,
                bitmap = bitmap,
                progressStep = "Scanning vehicle outline & features..."
            )

            // 4. Request AI Analysis
            val result = repository.analyzeCarImage(bitmap)

            result.onSuccess { analysisResult ->
                _uiState.value = AnalysisUiState.Success(
                    imageUri = uri,
                    bitmap = bitmap,
                    result = analysisResult
                )
            }.onFailure { exception ->
                _uiState.value = AnalysisUiState.Error(
                    imageUri = uri,
                    bitmap = bitmap,
                    message = exception.message ?: "Failed to analyze car image.",
                    errorType = ErrorType.API_ERROR
                )
            }
        }
    }

    /**
     * Resets state back to initial Idle state (Home Screen).
     */
    fun resetToHome() {
        currentBitmap = null
        currentUri = null
        _uiState.value = AnalysisUiState.Idle
    }

    /**
     * Clears current image and returns to image picker state.
     */
    fun chooseAnotherImage() {
        resetToHome()
    }

    /**
     * API Key Management helpers.
     */
    fun isApiKeyConfigured(): Boolean = apiKeyStorage.isApiKeyConfigured()
    fun isApiKeyFromBuildConfig(): Boolean = apiKeyStorage.isApiKeyFromBuildConfig()
    fun getApiKey(): String = apiKeyStorage.getApiKey()
    fun saveCustomApiKey(key: String) {
        apiKeyStorage.saveCustomApiKey(key)
    }
    fun clearCustomApiKey() {
        apiKeyStorage.clearCustomApiKey()
    }
}
