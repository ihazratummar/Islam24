package com.hazrat.tasbih.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

/**
 * Tasbih Action Controls: Tap to Count, Reset, and Undo buttons.
 * @author hazratummar
 */
@Composable
fun TasbihActionButtons(
    onCount: () -> Unit,
    onReset: () -> Unit,
    onUndo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.space12)
    ) {
        // Primary Count Button
        Button(
            onClick = onCount,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.compButton),
            shape = RoundedCornerShape(dimens.cornerLg),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.tap),
                contentDescription = "Tap to Count",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(dimens.iconSm)
            )
            Spacer(modifier = Modifier.width(dimens.space8))
            Text(
                text = "Tap to Count",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Secondary Action Buttons: Reset & Undo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            Button(
                onClick = onReset,
                modifier = Modifier
                    .weight(1f)
                    .height(dimens.compButton),
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.refresh),
                    contentDescription = "Reset",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(dimens.iconSm)
                )
                Spacer(modifier = Modifier.width(dimens.space8))
                Text(
                    text = "Reset",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onUndo,
                modifier = Modifier
                    .weight(1f)
                    .height(dimens.compButton),
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.undo),
                    contentDescription = "Undo",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(dimens.iconSm)
                )
                Spacer(modifier = Modifier.width(dimens.space8))
                Text(
                    text = "Undo",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
