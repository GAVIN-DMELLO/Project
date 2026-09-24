package com.example.cargrasp.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cargrasp.data.model.CarAnalysisResult
import com.example.cargrasp.ui.components.CarDetectionStatusBadge
import com.example.cargrasp.ui.components.ColorSwatchBadge
import com.example.cargrasp.ui.components.ConfidenceMeter
import com.example.cargrasp.ui.components.PrimaryGradientButton
import com.example.cargrasp.ui.components.SecondaryOutlineButton
import com.example.cargrasp.ui.components.SpecItemCard
import com.example.cargrasp.ui.theme.AmberWarning
import com.example.cargrasp.ui.theme.BackgroundDark
import com.example.cargrasp.ui.theme.CyanNeon
import com.example.cargrasp.ui.theme.EmeraldSuccess
import com.example.cargrasp.ui.theme.EmeraldSuccessContainer
import com.example.cargrasp.ui.theme.RedError
import com.example.cargrasp.ui.theme.SurfaceCard
import com.example.cargrasp.ui.theme.SurfaceCardElevated

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    bitmap: Bitmap,
    result: CarAnalysisResult,
    onAnalyzeAnotherClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Recognition Results",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Share results button
                    if (result.carDetected) {
                        IconButton(onClick = { shareCarResults(context, result) }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = CyanNeon
                            )
                        }
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
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Image Thumbnail Card with Detection Badge
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color(0xFF263554), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Analyzed Car",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xCC0A0F1D))
                                )
                            )
                    )

                    // Detection Status Badge floating on image
                    CarDetectionStatusBadge(
                        detected = result.carDetected,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (result.carDetected) {
                // Main Car Title Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = result.getFullTitle(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ConfidenceMeter(confidence = result.confidence)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Core Vehicle Specs
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Make / Manufacturer
                    SpecItemCard(
                        icon = Icons.Default.DirectionsCar,
                        label = "Make / Manufacturer",
                        value = result.make
                    )

                    // Model
                    SpecItemCard(
                        icon = Icons.Default.CarRental,
                        label = "Model",
                        value = result.model
                    )

                    // Colour with live Swatch
                    SpecItemCard(
                        icon = Icons.Default.ColorLens,
                        label = "Colour",
                        value = result.colour,
                        customValueContent = {
                            ColorSwatchBadge(colorName = result.colour)
                        }
                    )

                    // Body Type & Year (if available)
                    if (result.bodyType.isNotBlank() && !result.bodyType.equals("Unknown", true)) {
                        SpecItemCard(
                            icon = Icons.Default.CalendarMonth,
                            label = "Body Type & Generation",
                            value = "${result.bodyType} ${if (result.estimatedYearRange != "Unknown") "• ${result.estimatedYearRange}" else ""}".trim()
                        )
                    }

                    // AI Insights / Notes
                    if (result.notes.isNotBlank()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF263554))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = CyanNeon,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "AI Vision Insights",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = CyanNeon
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = result.notes,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFCBD5E1),
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            } else {
                // Car Not Detected Screen Content
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = AmberWarning,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Car Not Detected",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (result.notes.isNotBlank()) result.notes else "The AI could not identify a clear automobile in this photo. Try taking a closer picture showing the front grille, badge, or full body profile.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF94A3B8),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Button: "Analyze Another Car"
            PrimaryGradientButton(
                text = "Analyze Another Car",
                icon = Icons.Default.Refresh,
                onClick = onAnalyzeAnotherClick
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun shareCarResults(context: Context, result: CarAnalysisResult) {
    val shareText = """
        🚗 CarGrasp AI Vehicle Recognition Result:
        • Make: ${result.make}
        • Model: ${result.model}
        • Colour: ${result.colour}
        • Body Type: ${result.bodyType}
        • AI Confidence: ${result.confidence}
        
        Identified with CarGrasp (Powered by Gemini AI)
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Car Identification - ${result.getFullTitle()}")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Share Car Recognition Result"))
}
