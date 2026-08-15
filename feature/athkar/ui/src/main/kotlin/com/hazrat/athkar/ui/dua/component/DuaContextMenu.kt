package com.hazrat.athkar.ui.dua.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import com.hazrat.model.DuaItemModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Context Menu for a Dua item with Bookmark, Copy, and Share actions positioned at the user's tap location.
 * @author hazratummar
 */
@Composable
fun DuaContextMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    dua: DuaItemModel,
    onBookmarkClick: () -> Unit,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset.Zero
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = offset,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(if (dua.isBookmarked) R.string.dua_bookmarked else R.string.dua_bookmark),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (dua.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.favourite),
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconSm),
                    tint = if (dua.isBookmarked) MaterialTheme.colorScheme.primary else customColors.secondaryText
                )
            },
            onClick = {
                onDismissRequest()
                onBookmarkClick()
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.dua_copy),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.copy_outline),
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconSm),
                    tint = customColors.secondaryText
                )
            },
            onClick = {
                onDismissRequest()
                onCopyClick()
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.dua_share),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconSm),
                    tint = customColors.secondaryText
                )
            },
            onClick = {
                onDismissRequest()
                onShareClick()
            }
        )
    }
}
