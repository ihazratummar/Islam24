package com.hazrat.permission

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * A High-Order Component that gates content behind a permission check.
 * If permission is not granted, it shows a rationale UI or a placeholder.
 */
@Composable
fun PermissionGate(
    permission: String,
    rationaleTitle: String,
    rationaleMessage: String,
    onPermissionGranted: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isGranted = isPermissionGranted(context, permission)

    if (isGranted) {
        content()
    } else {
        PermissionRationaleDialog(
            title = rationaleTitle,
            message = rationaleMessage,
            onConfirm = {
                // This would trigger the requester. 
                // Since rememberPermissionRequester is a Composable, 
                // it's better used at the Screen level.
                // This gate serves as an automatic rationale provider.
            },
            onDismiss = { /* Handle dismissal if needed */ }
        )
    }
}
