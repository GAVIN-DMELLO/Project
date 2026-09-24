package com.example.cargrasp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.cargrasp.data.model.AnalysisUiState
import com.example.cargrasp.ui.screens.CameraCaptureScreen
import com.example.cargrasp.ui.screens.HomeScreen
import com.example.cargrasp.ui.screens.ImagePreviewScreen
import com.example.cargrasp.ui.screens.ResultsScreen
import com.example.cargrasp.ui.theme.BackgroundDark
import com.example.cargrasp.ui.theme.CarGraspTheme
import com.example.cargrasp.ui.viewmodel.CarGraspViewModel

/**
 * Main Activity of CarGrasp - Coordinates camera capture, gallery picking, and Compose navigation.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: CarGraspViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CarGraspTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundDark
                ) {
                    CarGraspApp(viewModel = viewModel)
                }
            }
        }
    }
}

/**
 * Screen routes / modes for CarGrasp navigation.
 */
enum class AppScreen {
    HOME,
    CAMERA_VIEWFINDER,
    PREVIEW,
    RESULTS
}

@Composable
fun CarGraspApp(viewModel: CarGraspViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }

    // Modern Android Photo Picker Contract for Gallery Image Selection
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onImageSelectedFromGallery(uri)
            currentScreen = AppScreen.PREVIEW
        }
    }

    // Camera Permission Launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            currentScreen = AppScreen.CAMERA_VIEWFINDER
        }
    }

    // Determine current screen dynamically from UI State or screen navigation
    when {
        currentScreen == AppScreen.CAMERA_VIEWFINDER -> {
            CameraCaptureScreen(
                onPhotoCaptured = { bitmap: Bitmap ->
                    viewModel.onPhotoCaptured(bitmap)
                    currentScreen = AppScreen.PREVIEW
                },
                onBackClick = {
                    currentScreen = AppScreen.HOME
                }
            )
        }

        uiState is AnalysisUiState.Success -> {
            val successState = uiState as AnalysisUiState.Success
            ResultsScreen(
                bitmap = successState.bitmap,
                result = successState.result,
                onAnalyzeAnotherClick = {
                    viewModel.resetToHome()
                    currentScreen = AppScreen.HOME
                },
                onBackClick = {
                    viewModel.resetToHome()
                    currentScreen = AppScreen.HOME
                }
            )
        }

        uiState is AnalysisUiState.ImageReady -> {
            val readyState = uiState as AnalysisUiState.ImageReady
            ImagePreviewScreen(
                bitmap = readyState.bitmap,
                uiState = uiState,
                onAnalyzeClick = {
                    viewModel.analyzeCar()
                },
                onChooseAnotherClick = {
                    viewModel.resetToHome()
                    currentScreen = AppScreen.HOME
                },
                onBackClick = {
                    viewModel.resetToHome()
                    currentScreen = AppScreen.HOME
                }
            )
        }

        uiState is AnalysisUiState.Analyzing -> {
            val analyzingState = uiState as AnalysisUiState.Analyzing
            ImagePreviewScreen(
                bitmap = analyzingState.bitmap,
                uiState = uiState,
                onAnalyzeClick = {},
                onChooseAnotherClick = {
                    viewModel.resetToHome()
                    currentScreen = AppScreen.HOME
                },
                onBackClick = {}
            )
        }

        uiState is AnalysisUiState.Error -> {
            val errorState = uiState as AnalysisUiState.Error
            if (errorState.bitmap != null) {
                ImagePreviewScreen(
                    bitmap = errorState.bitmap,
                    uiState = uiState,
                    onAnalyzeClick = {
                        viewModel.analyzeCar()
                    },
                    onChooseAnotherClick = {
                        viewModel.resetToHome()
                        currentScreen = AppScreen.HOME
                    },
                    onBackClick = {
                        viewModel.resetToHome()
                        currentScreen = AppScreen.HOME
                    }
                )
            } else {
                HomeScreen(
                    viewModel = viewModel,
                    onTakePhotoClick = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    onChooseGalleryClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }

        else -> {
            HomeScreen(
                viewModel = viewModel,
                onTakePhotoClick = {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onChooseGalleryClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
        }
    }
}
