package com.hazrat.prayer.ui.notification.component

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.Prayer
import com.hazrat.prayer.ui.component.GoldAccent
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

import android.media.RingtoneManager

data class AzanReciterOption(
    val name: String,
    val rawResId: Int
)

val fajrAzanOptions = listOf(
    AzanReciterOption("System Default", 0),
    AzanReciterOption("Fajr Azan 1", R.raw.fajr1),
    AzanReciterOption("Fajr Azan 2", R.raw.fajr2)
)

val standardAzanOptions = listOf(
    AzanReciterOption("System Default", 0),
    AzanReciterOption("Azan 1", R.raw.azan2),
    AzanReciterOption("Azan 2", R.raw.azan3),
    AzanReciterOption("Azan 3", R.raw.azan4),
    AzanReciterOption("Azan 4", R.raw.azan5),
    AzanReciterOption("Azan 5", R.raw.azan6),
    AzanReciterOption("Azan 6", R.raw.azan7),
    AzanReciterOption("Azan 7", R.raw.azan8),
    AzanReciterOption("Azan 8", R.raw.azan9),
    AzanReciterOption("Azan 9", R.raw.azan10)
)

/**
 * Modal Bottom Sheet for selecting Azan Reciter sound with live audio preview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AzanSoundBottomSheet(
    prayer: Prayer,
    currentSelectedSound: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    val context = LocalContext.current
    val options = if (prayer == Prayer.FAJR) fajrAzanOptions else standardAzanOptions

    var selectedReciterName by remember(currentSelectedSound) { mutableStateOf(currentSelectedSound) }
    var playingRawResId by remember { mutableIntStateOf(-1) }

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    fun stopAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        playingRawResId = -1
    }

    fun playAudio(resId: Int) {
        stopAudio()
        try {
            val mp = if (resId == 0) {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                MediaPlayer.create(context, uri)
            } else {
                MediaPlayer.create(context, resId)
            }
            mp?.start()
            mp?.setOnCompletionListener {
                playingRawResId = -1
            }
            mediaPlayer = mp
            playingRawResId = resId
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            stopAudio()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            stopAudio()
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(topStart = dimens.cornerXl, topEnd = dimens.cornerXl)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.space20, vertical = dimens.space16),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // Drag Handle Indicator
            Box(
                modifier = Modifier
                    .width(dimens.space32)
                    .height(dimens.space4)
                    .clip(RoundedCornerShape(dimens.cornerFull))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    .align(Alignment.CenterHorizontally)
            )

            // Header Title & Subtitle
            Column(verticalArrangement = Arrangement.spacedBy(dimens.space4)) {
                Text(
                    text = stringResource(R.string.prayer_azan_sound),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.prayer_select_reciter, prayer.name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = customColors.secondaryText
                )
            }

            Spacer(modifier = Modifier.height(dimens.space4))

            // Reciters List with Audio Preview
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space56 * 4),
                verticalArrangement = Arrangement.spacedBy(dimens.space8)
            ) {
                items(options) { reciter ->
                    val isSelected = (selectedReciterName == reciter.name)
                    val isPlayingThis = (playingRawResId == reciter.rawResId)

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(dimens.cornerLg))
                            .clickable { selectedReciterName = reciter.name },
                        shape = RoundedCornerShape(dimens.cornerLg),
                        color = if (isSelected) GoldAccent.copy(alpha = 0.15f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
                        border = BorderStroke(
                            dimens.divider,
                            if (isSelected) GoldAccent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimens.space16, vertical = dimens.space12),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = reciter.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isSelected) GoldAccent else MaterialTheme.colorScheme.onSurface
                            )

                            // Audio Preview Play / Stop Button
                            Box(
                                modifier = Modifier
                                    .size(dimens.avatarMd)
                                    .clip(CircleShape)
                                    .background(if (isPlayingThis) GoldAccent else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                                    .clickable {
                                        if (isPlayingThis) {
                                            stopAudio()
                                        } else {
                                            playAudio(reciter.rawResId)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(if (isPlayingThis) R.drawable.stop else R.drawable.play),
                                    contentDescription = "Audio Preview",
                                    tint = if (isPlayingThis) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(dimens.iconSm)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.space8))

            // Done Action Button
            Button(
                onClick = {
                    stopAudio()
                    onConfirm(selectedReciterName)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space48),
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
            ) {
                Text(
                    text = stringResource(R.string.prayer_done),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(dimens.space16))
        }
    }
}
