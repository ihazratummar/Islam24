package com.hazrat.alQuran.ui.component

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

/**
 * Floating Audio Controller bar matching user reference screenshots 2 & 3.
 * Features: Play/Pause, Speed Toggle (1x, 1.25x, 1.5x, 2x), Prev/Next, Reciter Name, Download/Playback progress bar.
 *
 * @author hazratummar
 */
@Composable
fun FloatingAudioController(
    modifier: Modifier = Modifier,
    surahName: String,
    ayahNumber: Int,
    reciterName: String = "Mishary Rashid Alafasy",
    isPlaying: Boolean,
    isDownloading: Boolean,
    downloadProgress: Float,
    playbackSpeed: Float,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onCloseClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.space16, vertical = dimens.space12),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.space8)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.space16, vertical = dimens.space12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Play / Pause / Download Progress Button
                Box(
                    modifier = Modifier
                        .size(dimens.space48)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(enabled = !isDownloading) { onPlayPauseClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (isDownloading) {
                        CircularProgressIndicator(
                            progress = { downloadProgress },
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = dimens.divider,
                            modifier = Modifier.size(dimens.space24)
                        )
                    } else {
                        Icon(
                            painter = painterResource(
                                id = if (isPlaying) R.drawable.ic_pause else R.drawable.play
                            ),
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(dimens.iconMd)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(dimens.space12))

                // 2. Surah Name, Ayah Number & Reciter Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Text(
                        text = "$surahName, Aya $ayahNumber",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    Text(
                        text = if (isDownloading) "Downloading audio..." else reciterName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                // 3. Playback Speed Selector (1x, 1.25x, 1.5x, 2x)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimens.cornerMd))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            width = dimens.divider,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(dimens.cornerMd)
                        )
                        .clickable {
                            val nextSpeed = when (playbackSpeed) {
                                1.0f -> 1.25f
                                1.25f -> 1.5f
                                1.5f -> 2.0f
                                else -> 1.0f
                            }
                            onSpeedChange(nextSpeed)
                        }
                        .padding(horizontal = dimens.space8, vertical = dimens.space4),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${playbackSpeed}x",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(dimens.space4))

                // 4. Previous Ayah (<)
                IconButton(
                    onClick = onPreviousClick,
                    modifier = Modifier.size(dimens.space32)
                ) {
                    Text(
                        text = "◀",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // 5. Next Ayah (>)
                IconButton(
                    onClick = onNextClick,
                    modifier = Modifier.size(dimens.space32)
                ) {
                    Text(
                        text = "▶",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // 6. Close Controller (✕)
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier.size(dimens.space32)
                ) {
                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Bottom Progress Bar (Industry Standard Download & Buffer Progress)
            if (isDownloading) {
                LinearProgressIndicator(
                    progress = { downloadProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.space4),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            }
        }
    }
}
