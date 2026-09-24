package com.example.cargrasp.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cargrasp.ui.theme.CyanNeon
import com.example.cargrasp.ui.theme.IndigoVibrant

/**
 * High-tech scanner overlay animation rendered on top of the car image during AI processing.
 */
@Composable
fun CarScannerOverlay(
    modifier: Modifier = Modifier,
    statusMessage: String = "Analyzing vehicle features..."
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")

    // Vertical sweep for scanning laser line
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanLine"
    )

    // Pulse effect for corner target reticles
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
    ) {
        // Futuristic Cyber Canvas Overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val currentY = height * scanProgress

            // 1. Draw glowing horizontal scan beam
            val beamHeight = 40.dp.toPx()
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    CyanNeon.copy(alpha = 0.25f),
                    CyanNeon.copy(alpha = 0.85f),
                    CyanNeon.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                startY = currentY - beamHeight / 2,
                endY = currentY + beamHeight / 2
            )
            drawRect(
                brush = gradientBrush,
                topLeft = Offset(0f, currentY - beamHeight / 2),
                size = androidx.compose.ui.geometry.Size(width, beamHeight)
            )

            // 2. Draw solid bright laser core line
            drawLine(
                color = Color.White,
                start = Offset(0f, currentY),
                end = Offset(width, currentY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // 3. Draw viewfinder corner brackets
            val cornerLen = 36.dp.toPx()
            val cornerStroke = 4.dp.toPx()
            val cornerColor = CyanNeon.copy(alpha = pulseAlpha)

            // Top-Left
            drawLine(cornerColor, Offset(16f, 16f), Offset(16f + cornerLen, 16f), cornerStroke)
            drawLine(cornerColor, Offset(16f, 16f), Offset(16f, 16f + cornerLen), cornerStroke)

            // Top-Right
            drawLine(cornerColor, Offset(width - 16f, 16f), Offset(width - 16f - cornerLen, 16f), cornerStroke)
            drawLine(cornerColor, Offset(width - 16f, 16f), Offset(width - 16f, 16f + cornerLen), cornerStroke)

            // Bottom-Left
            drawLine(cornerColor, Offset(16f, height - 16f), Offset(16f + cornerLen, height - 16f), cornerStroke)
            drawLine(cornerColor, Offset(16f, height - 16f), Offset(16f, height - 16f - cornerLen), cornerStroke)

            // Bottom-Right
            drawLine(cornerColor, Offset(width - 16f, height - 16f), Offset(width - 16f - cornerLen, height - 16f), cornerStroke)
            drawLine(cornerColor, Offset(width - 16f, height - 16f), Offset(width - 16f, height - 16f - cornerLen), cornerStroke)
        }

        // Status Card at Bottom
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xDD0A0F1D),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = CyanNeon,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = statusMessage,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
