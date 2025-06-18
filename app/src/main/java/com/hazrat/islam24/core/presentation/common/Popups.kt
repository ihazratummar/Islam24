package com.hazrat.islam24.core.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

/**
 * @author Hazrat Ummar Shaikh
 * Created on 27-01-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopupDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    title: String,
    text: String,
    icon: Int,
    confirmButton: () -> Unit = {}
) {

    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null
            )
        },
        title = { Text(text = title) },
        text = { Text(text = text) },
        onDismissRequest = {  },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) { Text("Dismiss") }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    confirmButton()
                    onDismissRequest()
                }
            ) {
                Text("Confirm")
            }
        }
    )

}