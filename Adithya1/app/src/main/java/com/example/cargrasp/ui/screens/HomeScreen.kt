package com.example.cargrasp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cargrasp.R
import com.example.cargrasp.ui.components.PrimaryGradientButton
import com.example.cargrasp.ui.components.SecondaryOutlineButton
import com.example.cargrasp.ui.theme.BackgroundDark
import com.example.cargrasp.ui.theme.BlueNeon
import com.example.cargrasp.ui.theme.CyanNeon
import com.example.cargrasp.ui.theme.EmeraldSuccess
import com.example.cargrasp.ui.theme.IndigoVibrant
import com.example.cargrasp.ui.theme.SurfaceCard
import com.example.cargrasp.ui.theme.SurfaceCardElevated
import com.example.cargrasp.ui.viewmodel.CarGraspViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CarGraspViewModel,
    onTakePhotoClick: () -> Unit,
    onChooseGalleryClick: () -> Unit
) {
    var showApiKeyDialog by remember { mutableStateOf(false) }
    val isApiKeySet = viewModel.isApiKeyConfigured()

    if (showApiKeyDialog) {
        ApiKeyDialog(
            viewModel = viewModel,
            onDismiss = { showApiKeyDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(listOf(CyanNeon, BlueNeon))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "CarGrasp",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    // API Key Settings Chip / Icon
                    Surface(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showApiKeyDialog = true },
                        color = if (isApiKeySet) Color(0x2210B981) else Color(0x33F59E0B),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isApiKeySet) EmeraldSuccess.copy(alpha = 0.5f) else Color(0xFFF59E0B)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = "API Key",
                                tint = if (isApiKeySet) EmeraldSuccess else Color(0xFFF59E0B),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isApiKeySet) "API Active" else "Setup Key",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isApiKeySet) EmeraldSuccess else Color(0xFFF59E0B)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
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
            Spacer(modifier = Modifier.height(8.dp))

            // Hero Showcase Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.4f), Color.Transparent)), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Futuristic glowing car emblem
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(CyanNeon.copy(alpha = 0.25f), Color.Transparent)
                                )
                            )
                            .border(1.5.dp, CyanNeon.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = CyanNeon,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Instant Car Recognition",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Identify a car's make, model and colour using AI.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Feature Highlights Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeatureBadge(
                    icon = Icons.Default.DirectionsCar,
                    title = "Make & Model",
                    modifier = Modifier.weight(1f)
                )
                FeatureBadge(
                    icon = Icons.Default.ColorLens,
                    title = "Colour Detect",
                    modifier = Modifier.weight(1f)
                )
                FeatureBadge(
                    icon = Icons.Default.AutoAwesome,
                    title = "Gemini AI",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PrimaryGradientButton(
                    text = "Take Photo",
                    icon = Icons.Default.CameraAlt,
                    onClick = onTakePhotoClick
                )

                SecondaryOutlineButton(
                    text = "Choose from Gallery",
                    icon = Icons.Default.PhotoLibrary,
                    onClick = onChooseGalleryClick
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Student Project Footer
            Text(
                text = "CarGrasp • AI Computer Vision Project",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FeatureBadge(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF263554))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CyanNeon,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE2E8F0),
                textAlign = TextAlign.Center
            )
        }
    }
}
