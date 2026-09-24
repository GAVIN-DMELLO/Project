package com.example.cargrasp.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cargrasp.data.model.AnalysisUiState
import com.example.cargrasp.data.model.ErrorType
import com.example.cargrasp.ui.components.CarScannerOverlay
import com.example.cargrasp.ui.components.PrimaryGradientButton
import com.example.cargrasp.ui.components.SecondaryOutlineButton
import com.example.cargrasp.ui.theme.BackgroundDark
import com.example.cargrasp.ui.theme.CyanNeon
import com.example.cargrasp.ui.theme.RedError
import com.example.cargrasp.ui.theme.RedErrorContainer
import com.example.cargrasp.ui.theme.SurfaceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePreviewScreen(
    bitmap: Bitmap,
    uiState: AnalysisUiState,
    onAnalyzeClick: () -> Unit,
    onChooseAnotherClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val isAnalyzing = uiState is AnalysisUiState.Analyzing
    val analyzingMessage = if (uiState is AnalysisUiState.Analyzing) uiState.progressStep else "Analyzing vehicle..."
    val errorState = uiState as? AnalysisUiState.Error

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Image Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, enabled = !isAnalyzing) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        containerColor = BackgroundDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Image Container Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .border(1.5.dp, CyanNeon.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Selected Car Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Cyber Laser Scanner overlay when analyzing
                    if (isAnalyzing) {
                        CarScannerOverlay(
                            modifier = Modifier.fillMaxSize(),
                            statusMessage = analyzingMessage
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error Card if something went wrong
            if (errorState != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = RedErrorContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, RedError.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = RedError,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (errorState.errorType == ErrorType.NO_INTERNET) "No Internet Connection" else "Analysis Error",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = RedError
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = errorState.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else if (!isAnalyzing) {
                // Helpful instruction chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceCard
                ) {
                    Text(
                        text = "Make sure the car is clearly visible for accurate recognition.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PrimaryGradientButton(
                    text = if (errorState != null) "Retry Analysis" else "Analyze Car",
                    icon = if (errorState != null) Icons.Default.Refresh else Icons.Default.AutoAwesome,
                    onClick = onAnalyzeClick,
                    enabled = !isAnalyzing
                )

                SecondaryOutlineButton(
                    text = "Choose Another Image",
                    icon = Icons.Default.Image,
                    onClick = onChooseAnotherClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
