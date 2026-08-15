package com.hazrat.athkar.ui.dua.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

/**
 * Quick Action Bar with styled badges for Bookmarks, Recents, Hisnul Muslim, and Tasbih.
 * @author hazratummar
 */
@Composable
fun DuaQuickActionBar(
    onBookmarksClick: () -> Unit,
    onRecentsClick: () -> Unit,
    onHisnulMuslimClick: () -> Unit,
    onTasbihClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.space16, horizontal = dimens.space8),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickActionItem(
                iconRes = R.drawable.favourite,
                iconTint = MaterialTheme.colorScheme.tertiary,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                label = stringResource(R.string.dua_bookmarks),
                onClick = onBookmarksClick
            )
            QuickActionItem(
                iconRes = R.drawable.refresh,
                iconTint = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                label = stringResource(R.string.dua_recents),
                onClick = onRecentsClick
            )
            QuickActionItem(
                iconRes = R.drawable.book,
                iconTint = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                label = stringResource(R.string.dua_hisnul_muslim),
                onClick = onHisnulMuslimClick
            )
            QuickActionItem(
                iconRes = R.drawable.tasbih,
                iconTint = Color.Unspecified,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                label = stringResource(R.string.dua_tasbih),
                onClick = onTasbihClick
            )
        }
    }
}

@Composable
private fun QuickActionItem(
    iconRes: Int,
    iconTint: Color,
    containerColor: Color,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.cornerMd))
            .clickable(onClick = onClick)
            .padding(horizontal = dimens.space8, vertical = dimens.space4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.space8)
    ) {
        Box(
            modifier = Modifier
                .size(dimens.avatarMd)
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(dimens.cornerMd)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(dimens.iconMd),
                tint = iconTint
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
