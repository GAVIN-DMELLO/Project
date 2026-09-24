package com.example.cargrasp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cargrasp.ui.theme.AmberWarning
import com.example.cargrasp.ui.theme.AmberWarningContainer
import com.example.cargrasp.ui.theme.BlueNeon
import com.example.cargrasp.ui.theme.CyanNeon
import com.example.cargrasp.ui.theme.EmeraldSuccess
import com.example.cargrasp.ui.theme.EmeraldSuccessContainer
import com.example.cargrasp.ui.theme.IndigoVibrant
import com.example.cargrasp.ui.theme.RedError
import com.example.cargrasp.ui.theme.RedErrorContainer
import com.example.cargrasp.ui.theme.SurfaceCard

/**
 * Premium gradient action button for primary call-to-actions.
 */
@Composable
fun PrimaryGradientButton(
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val gradient = Brush.horizontalGradient(
        colors = if (enabled) {
            listOf(CyanNeon, BlueNeon, IndigoVibrant)
        } else {
            listOf(Color.Gray, Color.DarkGray)
        }
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(if (enabled) 12.dp else 0.dp, shape = RoundedCornerShape(16.dp), spotColor = CyanNeon),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .clickable(enabled = enabled, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

/**
 * Secondary outlined button with subtle neon border glow.
 */
@Composable
fun SecondaryOutlineButton(
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, CyanNeon.copy(alpha = 0.6f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CyanNeon,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

/**
 * Badge pill indicating car detection status (Success Green or Warning Orange).
 */
@Composable
fun CarDetectionStatusBadge(
    detected: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor = if (detected) EmeraldSuccessContainer else AmberWarningContainer
    val contentColor = if (detected) EmeraldSuccess else AmberWarning
    val icon = if (detected) Icons.Default.CheckCircle else Icons.Default.Warning
    val label = if (detected) "Car Detected" else "Car Not Detected"

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, contentColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * AI Confidence Level Meter (High / Medium / Low / Uncertain).
 */
@Composable
fun ConfidenceMeter(
    confidence: String,
    modifier: Modifier = Modifier
) {
    val (color, levelRatio) = when (confidence.lowercase()) {
        "high" -> Pair(EmeraldSuccess, 1.0f)
        "medium" -> Pair(BlueNeon, 0.66f)
        "low" -> Pair(AmberWarning, 0.33f)
        else -> Pair(RedError, 0.15f)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AI Confidence",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = confidence.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        // Progress bar track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF263554))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(levelRatio)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}

/**
 * Color circle swatch for exterior car color.
 */
@Composable
fun ColorSwatchBadge(
    colorName: String,
    modifier: Modifier = Modifier
) {
    val swatchColor = parseExteriorColor(colorName)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(swatchColor)
                .border(1.5.dp, Color.White.copy(alpha = 0.6f), CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = colorName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

/**
 * Converts color name string to a representative Color for the UI swatch.
 */
private fun parseExteriorColor(name: String): Color {
    val lower = name.lowercase()
    return when {
        lower.contains("black") -> Color(0xFF18181B)
        lower.contains("white") || lower.contains("pearl") -> Color(0xFFF8FAFC)
        lower.contains("red") || lower.contains("crimson") || lower.contains("maroon") -> Color(0xFFDC2626)
        lower.contains("blue") || lower.contains("navy") -> Color(0xFF2563EB)
        lower.contains("silver") || lower.contains("gray") || lower.contains("grey") -> Color(0xFF94A3B8)
        lower.contains("yellow") || lower.contains("gold") -> Color(0xFFEAB308)
        lower.contains("green") || lower.contains("emerald") || lower.contains("olive") -> Color(0xFF16A34A)
        lower.contains("orange") -> Color(0xFFEA580C)
        lower.contains("brown") || lower.contains("bronze") -> Color(0xFF78350F)
        lower.contains("purple") || lower.contains("violet") -> Color(0xFF7C3AED)
        else -> CyanNeon
    }
}

/**
 * Reusable Specification Card Item with icon, title and description.
 */
@Composable
fun SpecItemCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    customValueContent: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF263554))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CyanNeon,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
                Spacer(modifier = Modifier.height(2.dp))
                if (customValueContent != null) {
                    customValueContent()
                } else {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
