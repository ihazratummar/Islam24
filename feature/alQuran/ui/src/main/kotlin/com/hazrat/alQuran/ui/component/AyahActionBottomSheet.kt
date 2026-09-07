package com.hazrat.alQuran.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.quran.AyahModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

/**
 * Modal Bottom Sheet for Ayah action selection.
 * Contains ONLY 2 options: Play & Share.
 *
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahActionBottomSheet(
    sheetState: SheetState,
    surahName: String,
    ayah: AyahModel,
    onDismissRequest: () -> Unit,
    onPlayClick: () -> Unit,
    onShareClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimens.space32)
        ) {
            // Header with Close Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.space20, vertical = dimens.space8),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = surahName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${stringResource(R.string.quran_aya_short)} ${ayah.surahNumber}:${ayah.ayahNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onDismissRequest) {
                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(dimens.space8))

            // Option 1: Play
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDismissRequest()
                        onPlayClick()
                    }
                    .padding(horizontal = dimens.space20, vertical = dimens.space16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = stringResource(R.string.quran_play),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(dimens.iconMd)
                )
                Spacer(modifier = Modifier.width(dimens.space16))
                Text(
                    text = stringResource(R.string.quran_play),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            // Option 2: Share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDismissRequest()
                        onShareClick()
                    }
                    .padding(horizontal = dimens.space20, vertical = dimens.space16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = stringResource(R.string.quran_share),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(dimens.iconMd)
                )
                Spacer(modifier = Modifier.width(dimens.space16))
                Text(
                    text = stringResource(R.string.quran_share),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
