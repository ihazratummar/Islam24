package com.hazrat.alQuran.ui.ayah

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazrat.ui.R

/**
 * Collapsible Tajweed color guide banner.
 * Shows all 16 tajweed rules with their colors in a beautiful flow layout.
 *
 * @author hazratummar
 */

private data class TajweedRule(
    val name: String,
    val arabicName: String,
    val color: Color,
    val description: String
)

private val tajweedRules = listOf(
    // Gray: Silent / Non-pronounced
    TajweedRule("Hamzat Wasl", "همزة وصل", Color(0xFFAAAAAA), "Silent Hamza"),
    TajweedRule("Lam Shamsiyyah", "لام شمسية", Color(0xFFAAAAAA), "Assimilated Lam"),
    TajweedRule("Silent", "حرف ساكن", Color(0xFFAAAAAA), "Non-pronounced"),

    // Blue: Madd (Prolongation)
    TajweedRule("Madd Normal", "مد طبيعي", Color(0xFF537FFF), "2 Vowels"),
    TajweedRule("Madd Permissible", "مد جائز", Color(0xFF4050FF), "2-6 Vowels"),
    TajweedRule("Madd Obligatory", "مد لازم", Color(0xFF4466DD), "4-5 Vowels"),
    TajweedRule("Madd Necessary", "مد واجب", Color(0xFF5A7FFF), "6 Vowels"),

    // Red: Qalqalah
    TajweedRule("Qalqalah", "قلقلة", Color(0xFFFF4444), "Echo Sound"),

    // Purple: Ikhfa
    TajweedRule("Ikhfa", "إخفاء", Color(0xFFCC44FF), "Concealment"),
    TajweedRule("Ikhfa Shafawi", "إخفاء شفوي", Color(0xFFEE55DD), "Lip Concealment"),

    // Green: Idgham
    TajweedRule("Idgham Ghunnah", "إدغام بغنة", Color(0xFF22CC99), "Merge + Nasalization"),
    TajweedRule("Idgham No Ghunnah", "إدغام بلا غنة", Color(0xFF22BB44), "Merge w/o Nasal"),
    TajweedRule("Idgham Shafawi", "إدغام شفوي", Color(0xFF77DD22), "Lip Merge"),

    // Cyan: Iqlab
    TajweedRule("Iqlab", "إقلاب", Color(0xFF44DDFF), "Conversion"),

    // Orange: Ghunnah
    TajweedRule("Ghunnah", "غنة", Color(0xFFFF9933), "Nasalization"),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TajweedGuideBanner(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "arrow_rotation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E1E1E),
                        Color(0xFF2A2A2A)
                    )
                )
            )
    ) {
        // Header — always visible, tappable to toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎨",
                    fontSize = 16.sp
                )
                Text(
                    text = "Tajweed Colors",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                )
            }
            Icon(
                painter = painterResource(R.drawable.down_arrow),
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotationAngle)
            )
        }

        // Expandable content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tajweedRules.forEach { rule ->
                        TajweedColorChip(rule)
                    }
                }
            }
        }
    }
}

@Composable
private fun TajweedColorChip(rule: TajweedRule) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color dot
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(rule.color)
        )
        // Rule name
        Text(
            text = rule.name,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
            )
        )
    }
}
