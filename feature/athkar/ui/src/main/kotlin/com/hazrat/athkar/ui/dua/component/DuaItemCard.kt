package com.hazrat.athkar.ui.dua.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.hazrat.model.DuaItemModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

private fun String.cleanUthmanic(): String {
    return this
        .replace("\u06DF", "")
        .replace('\u0652', '\u06e1')
        .replace("\uFEFF", "")
        .trim()
}

/**
 * Individual Dua Item Card displaying Arabic, Translation, Reference, and Context Menu at exact tap coordinates.
 * @author hazratummar
 */
@Composable
fun DuaItemCard(
    dua: DuaItemModel,
    onToggleBookmark: (Boolean) -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val currentDimens = dimens
    var isMenuExpanded by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerMd))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        pressOffset = offset
                        isMenuExpanded = true
                    }
                )
            }
            .padding(vertical = dimens.space12)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            // Header row with Dua index pill badge and context menu button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Index Pill Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(dimens.cornerFull))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .padding(horizontal = dimens.space12, vertical = dimens.space4)
                    ) {
                        Text(
                            text = "${dua.id}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Repeat count badge if greater than 1
                    if (dua.repeatCount > 1) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimens.cornerFull))
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                            .padding(horizontal = dimens.space8, vertical = dimens.space2)
                        ) {
                            Text(
                                text = stringResource(R.string.dua_repeat_count, dua.repeatCount),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        pressOffset = with(density) {
                            Offset(x = currentDimens.layoutXl.toPx(), y = 0f)
                        }
                        isMenuExpanded = true
                    },
                    modifier = Modifier.size(dimens.iconLg)
                ) {
                    Icon(
                        painter = painterResource(id = if (dua.isBookmarked) R.drawable.favourite else R.drawable.menu_01),
                        contentDescription = "Options",
                        modifier = Modifier.size(dimens.iconSm),
                        tint = if (dua.isBookmarked) MaterialTheme.colorScheme.primary else customColors.secondaryText
                    )
                }
            }

            // Arabic text with original Scheherazade font, size (30.sp), and line height (60.sp)
            val plainText = remember(dua.arabicText) { dua.arabicText.cleanUthmanic() }
            Text(
                text = plainText,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = ScheherazadeFontFamily,
                    color = MaterialTheme.colorScheme.onBackground,
                    textDirection = TextDirection.Rtl,
                    fontFeatureSettings = "cv62",
                    fontSize = 30.sp,
                    lineHeight = 60.sp
                )
            )

            // Transliteration if available
            if (dua.transliteration.isNotBlank()) {
                Text(
                    text = dua.transliteration,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = customColors.secondaryText
                    )
                )
            }

            // Translation
            Text(
                text = dua.translation,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            // Reference at bottom
            if (dua.reference.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconXs),
                        tint = customColors.secondaryText
                    )
                    Text(
                        text = dua.reference,
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = dimens.space8),
                thickness = dimens.divider,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )
        }

        // Invisible precise anchor box positioned exactly at tap coordinate
        Box(
            modifier = Modifier
                .offset(
                    x = with(density) { pressOffset.x.toDp() },
                    y = with(density) { pressOffset.y.toDp() }
                )
                .size(dimens.space2)
        ) {
            DuaContextMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
                dua = dua,
                onBookmarkClick = {
                    onToggleBookmark(!dua.isBookmarked)
                },
                onCopyClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val textToCopy = buildString {
                        append(dua.arabicText.cleanUthmanic())
                        append("\n\n")
                        if (dua.transliteration.isNotBlank()) {
                            append(dua.transliteration)
                            append("\n\n")
                        }
                        append(dua.translation)
                        if (dua.reference.isNotBlank()) {
                            append("\n\nReference: ")
                            append(dua.reference)
                        }
                        append("\n\nShared via Islam 24")
                    }
                    clipboard.setPrimaryClip(ClipData.newPlainText("Dua", textToCopy))
                    Toast.makeText(context, "Dua copied to clipboard",  Toast.LENGTH_SHORT).show()
                },
                onShareClick = onShareClick
            )
        }
    }
}
