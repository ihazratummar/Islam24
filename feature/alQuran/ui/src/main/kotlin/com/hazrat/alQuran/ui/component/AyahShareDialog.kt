package com.hazrat.alQuran.ui.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hazrat.model.quran.AyahModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.ImageShareUtils
import kotlinx.coroutines.launch

/**
 * Branded Quran Ayah Share Dialog capturing high-resolution exact visual cards.
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahShareDialog(
    ayah: AyahModel,
    surahName: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    translationSource: String = "MUHIUDDIN"
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    var selectedTheme by remember { mutableStateOf(QuranShareTheme.EMERALD) }
    var showStorySelectionSheet by remember { mutableStateOf(false) }

    val activeTranslation = remember(ayah, translationSource) {
        ayah.getTranslation(translationSource)
    }

    val shareCaption = remember(ayah, surahName, activeTranslation) {
        "${ayah.arabicText}\n\n${ayah.getActiveTransliteration()}\n\n$activeTranslation\n\n— [$surahName, Ayah ${ayah.surahNumber}:${ayah.ayahNumber}]"
    }

    fun captureAndShare(action: (android.net.Uri) -> Unit) {
        coroutineScope.launch {
            try {
                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                val uri = ImageShareUtils.saveBitmapToCache(
                    context = context,
                    bitmap = bitmap,
                    filename = "ayah_${ayah.surahNumber}_${ayah.ayahNumber}_${selectedTheme.name.lowercase()}.png"
                )
                action(uri)
            } catch (e: Exception) {
                e.printStackTrace()
                ImageShareUtils.shareImage(context, android.net.Uri.EMPTY, shareCaption)
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.quran_share),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    actions = {
                        IconButton(onClick = onDismissRequest) {
                            Icon(
                                painter = painterResource(id = R.drawable.cross),
                                contentDescription = "Close",
                                modifier = Modifier.size(dimens.iconSm),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    windowInsets = WindowInsets(top = dimens.space20)
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = dimens.space16)
                        .padding(top = dimens.space8, bottom = dimens.space24),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimens.space16)
                ) {
                    // Theme Selector Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.space8),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuranShareTheme.entries.forEach { theme ->
                            val isSelected = theme == selectedTheme
                            Box(
                                modifier = Modifier
                                    .size(dimens.iconLg)
                                    .clip(CircleShape)
                                    .background(theme.startColor)
                                    .border(
                                        width = if (isSelected) dimens.space2 else dimens.divider,
                                        color = if (isSelected) theme.accentColor else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedTheme = theme }
                            )
                        }
                    }

                    // Share Action Buttons Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.space4),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuranShareChannelItem(
                            iconRes = R.drawable.ic_whatsapp,
                            containerColor = Color(0xFF25D366),
                            label = stringResource(R.string.dua_share_whatsapp),
                            onClick = {
                                captureAndShare { uri ->
                                    ImageShareUtils.shareToWhatsApp(context, uri, shareCaption)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                        QuranShareChannelItem(
                            iconRes = R.drawable.ic_instagram,
                            containerColor = Color(0xFFE1306C),
                            label = stringResource(R.string.dua_share_stories),
                            onClick = {
                                showStorySelectionSheet = true
                            },
                            modifier = Modifier.weight(1f)
                        )

                        QuranShareChannelItem(
                            iconRes = R.drawable.ic_messages,
                            containerColor = Color(0xFF0084FF),
                            label = stringResource(R.string.dua_share_messages),
                            onClick = {
                                captureAndShare { uri ->
                                    ImageShareUtils.shareToMessages(context, uri, shareCaption)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                        QuranShareChannelItem(
                            iconRes = R.drawable.menu_01,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            label = stringResource(R.string.dua_share_more),
                            onClick = {
                                captureAndShare { uri ->
                                    ImageShareUtils.shareImage(context, uri, shareCaption)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = dimens.space16)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Visual Share Card Preview with exact bitmap recording
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.space8)
                        .drawWithContent {
                            graphicsLayer.record {
                                this@drawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AyahShareCard(
                        ayah = ayah,
                        surahName = surahName,
                        theme = selectedTheme,
                        translationSource = translationSource
                    )
                }
            }
        }

        // Story Platform Selection Bottom Sheet
        if (showStorySelectionSheet) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = { showStorySelectionSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = dimens.space20)
                        .padding(bottom = dimens.space32),
                    verticalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    Text(
                        text = stringResource(R.string.dua_choose_story_platform),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    HorizontalDivider(
                        thickness = dimens.divider,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )

                    QuranStoryOptionItem(
                        iconRes = R.drawable.ic_instagram,
                        containerColor = Color(0xFFE1306C),
                        title = stringResource(R.string.dua_story_instagram),
                        onClick = {
                            showStorySelectionSheet = false
                            captureAndShare { uri ->
                                ImageShareUtils.shareToInstagram(context, uri)
                            }
                        }
                    )

                    QuranStoryOptionItem(
                        iconRes = R.drawable.ic_facebook,
                        containerColor = Color(0xFF1877F2),
                        title = stringResource(R.string.dua_story_facebook),
                        onClick = {
                            showStorySelectionSheet = false
                            captureAndShare { uri ->
                                ImageShareUtils.shareToFacebookStory(context, uri)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuranShareChannelItem(
    iconRes: Int,
    containerColor: Color,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.space8)
    ) {
        Box(
            modifier = Modifier
                .size(dimens.avatarLg)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = if (containerColor == MaterialTheme.colorScheme.surfaceContainerHighest) MaterialTheme.colorScheme.onSurface else Color.White,
                modifier = Modifier.size(dimens.iconMd)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun QuranStoryOptionItem(
    iconRes: Int,
    containerColor: Color,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerMd))
            .clickable(onClick = onClick)
            .padding(dimens.space12),
        horizontalArrangement = Arrangement.spacedBy(dimens.space16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dimens.avatarLg)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(dimens.iconMd)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
