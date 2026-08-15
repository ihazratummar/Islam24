package com.hazrat.athkar.ui.dua.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.hazrat.model.DuaChapterWithCountModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Chapter item row displaying icon, title, Dua count, and chevron.
 * @author hazratummar
 */
@Composable
fun DuaChapterListItem(
    chapter: DuaChapterWithCountModel,
    onClick: () -> Unit,
    showDivider: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.space16, horizontal = dimens.space16),
            horizontalArrangement = Arrangement.spacedBy(dimens.space16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.book),
                contentDescription = null,
                modifier = Modifier.size(dimens.iconMd),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space4)
            ) {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.dua_items_count, chapter.duaCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = customColors.secondaryText
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSm),
                tint = customColors.secondaryText
            )
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = dimens.space16),
                thickness = dimens.divider,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        }
    }
}
