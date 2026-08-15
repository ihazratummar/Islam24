package com.hazrat.athkar.ui.dua.dua_details

import androidx.compose.foundation.background
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hazrat.athkar.ui.dua.component.DuaShareCard
import com.hazrat.athkar.ui.dua.component.DuaShareTheme
import com.hazrat.athkar.ui.dua.component.DuaShareThemeSelector
import com.hazrat.athkar.ui.dua.utils.DuaShareUtils
import com.hazrat.model.DuaItemModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

/**
 * Branded Islamic Dua Share Dialog with customizable themes, dedicated platform icons, and multi-channel sharing.
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaShareDialog(
    dua: DuaItemModel,
    chapterTitle: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTheme by remember { mutableStateOf(DuaShareTheme.EMERALD) }
    var showStorySelectionSheet by remember { mutableStateOf(false) }

    val shareText = remember(dua, chapterTitle) {
        DuaShareUtils.buildShareText(dua, chapterTitle)
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
                            text = stringResource(R.string.dua_share_title),
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
                    // Theme Selector
                    DuaShareThemeSelector(
                        selectedTheme = selectedTheme,
                        onThemeSelect = { selectedTheme = it }
                    )

                    // Share Action Buttons Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.space4),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShareChannelItem(
                            iconRes = R.drawable.ic_whatsapp,
                            containerColor = Color(0xFF25D366),
                            label = stringResource(R.string.dua_share_whatsapp),
                            onClick = {
                                DuaShareUtils.shareToWhatsApp(context, shareText)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        ShareChannelItem(
                            iconRes = R.drawable.ic_instagram,
                            containerColor = Color(0xFFE1306C),
                            label = stringResource(R.string.dua_share_stories),
                            onClick = {
                                showStorySelectionSheet = true
                            },
                            modifier = Modifier.weight(1f)
                        )

                        ShareChannelItem(
                            iconRes = R.drawable.ic_messages,
                            containerColor = Color(0xFF0084FF),
                            label = stringResource(R.string.dua_share_messages),
                            onClick = {
                                DuaShareUtils.shareToMessages(context, shareText)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        ShareChannelItem(
                            iconRes = R.drawable.menu_01,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            label = stringResource(R.string.dua_share_more),
                            onClick = {
                                DuaShareUtils.shareText(context, shareText, targetPackage = null)
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
                // Visual Share Card Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.space8),
                    contentAlignment = Alignment.Center
                ) {
                    DuaShareCard(
                        dua = dua,
                        chapterTitle = chapterTitle,
                        theme = selectedTheme
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

                    StoryOptionItem(
                        iconRes = R.drawable.ic_instagram,
                        containerColor = Color(0xFFE1306C),
                        title = stringResource(R.string.dua_story_instagram),
                        onClick = {
                            showStorySelectionSheet = false
                            DuaShareUtils.shareToInstagram(context, shareText)
                        }
                    )

                    StoryOptionItem(
                        iconRes = R.drawable.ic_facebook,
                        containerColor = Color(0xFF1877F2),
                        title = stringResource(R.string.dua_story_facebook),
                        onClick = {
                            showStorySelectionSheet = false
                            DuaShareUtils.shareToFacebook(context, shareText)
                        }
                    )

                    StoryOptionItem(
                        iconRes = R.drawable.ic_whatsapp,
                        containerColor = Color(0xFF25D366),
                        title = stringResource(R.string.dua_story_whatsapp),
                        onClick = {
                            showStorySelectionSheet = false
                            DuaShareUtils.shareToWhatsApp(context, shareText)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ShareChannelItem(
    iconRes: Int,
    containerColor: Color,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.cornerMd))
            .clickable(onClick = onClick)
            .padding(vertical = dimens.space4),
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
                modifier = Modifier.size(dimens.iconMd),
                tint = Color.White
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StoryOptionItem(
    iconRes: Int,
    containerColor: Color,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerMd))
            .clickable(onClick = onClick)
            .padding(vertical = dimens.space8, horizontal = dimens.space8),
        horizontalArrangement = Arrangement.spacedBy(dimens.space16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dimens.avatarMd)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(dimens.iconSm),
                tint = Color.White
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
