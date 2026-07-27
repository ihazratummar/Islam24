package com.hazrat.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun rememberPermissionRequester(
    permission: String?,
    onGranted: () -> Unit,
    onDenied: () -> Unit = {},
    onPermissionDenied: () -> Unit = {}
): () -> Unit {

    val activity = LocalActivity.current as Activity // ✅ captured during composition

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                onGranted()
            } else {
                permission?.let {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                        onDenied()            // ✅ temporarily denied
                    } else {
                        onPermissionDenied()  // ✅ permanently denied ("Don't ask again")
                    }
                }
            }

        }

    return {
        when {
            permission == null || ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                onGranted()
            }

            else -> {
                launcher.launch(permission)
            }
        }
    }
}

fun isPermissionGranted(
    context: Context,
    permission: String?
): Boolean {
    if (permission == null) return true
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}