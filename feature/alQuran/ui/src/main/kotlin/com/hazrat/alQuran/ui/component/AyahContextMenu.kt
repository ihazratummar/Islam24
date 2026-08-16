package com.hazrat.alQuran.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.quran.AyahModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

/**
 * Anchored Context Menu for Quran Ayah interactions.
 * Dynamically adapts to Light Mode and Dark Mode.
 *
 * @author hazratummar
 */
@Composable
fun AyahContextMenu(
    expanded: Boolean,
    ayah: AyahModel,
    onDismissRequest: () -> Unit,
    onPlaySurahClick: () -> Unit,
    onPlayJuzClick: () -> Unit,
    onRepeatAyahClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .width(dimens.space48 * 5)
            .padding(vertical = dimens.space4),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(dimens.cornerLg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.space8),
            verticalArrangement = Arrangement.spacedBy(dimens.space2)
        ) {
            // Play to end of Surah
            ContextMenuItem(
                iconRes = R.drawable.play,
                title = "Play to the end of Surah",
                onClick = {
                    onPlaySurahClick()
                    onDismissRequest()
                }
            )

            // Play to end of Juz
            ContextMenuItem(
                iconRes = R.drawable.quran,
                title = "Play to the end of Juz",
                onClick = {
                    onPlayJuzClick()
                    onDismissRequest()
                }
            )

            // Repeat selected Ayah
            ContextMenuItem(
                iconRes = R.drawable.repeat,
                title = "Repeat selected Ayah",
                onClick = {
                    onRepeatAyahClick()
                    onDismissRequest()
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimens.space4),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Add to Favorites / Bookmark
            ContextMenuItem(
                iconRes = if (ayah.isBookmarked) R.drawable.star else R.drawable.outlinstar,
                title = if (ayah.isBookmarked) "Remove Favorite" else "Add to Favorites",
                onClick = {
                    onBookmarkClick()
                    onDismissRequest()
                }
            )

            // Copy
            ContextMenuItem(
                iconRes = R.drawable.copy_outline,
                title = "Copy",
                onClick = {
                    onCopyClick()
                    onDismissRequest()
                }
            )

            // Share
            ContextMenuItem(
                iconRes = R.drawable.share,
                title = "Share",
                onClick = {
                    onShareClick()
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
private fun ContextMenuItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerSm))
            .clickable(onClick = onClick)
            .padding(horizontal = dimens.space12, vertical = dimens.space8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimens.space12)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(dimens.iconSm)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}
