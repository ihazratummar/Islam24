package com.hazrat.permission

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.ui.R
import com.hazrat.ui.theme.Islam24Theme

/**
 * A standard rationale dialog to explain why a permission is needed.
 * Part of the industry-standard "Rationale First" approach.
 */

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PermissionRationaleDialog(
    modifier: Modifier = Modifier,
    title: String = "Hello",
    message: String = "We need permission",
    icon: Int = R.drawable.splash_logo,
    confirmLabel: String = "Allow",
    dismissLabel: String = "Not Now",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Islam24Theme {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(confirmLabel)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismiss) {
                    Text(dismissLabel)
                }
            }
        )
    }
}
