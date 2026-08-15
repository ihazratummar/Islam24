package com.hazrat.tasbih.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.theme.dimens

/**
 * Dialog for adding a custom user Dhikr.
 * @author hazratummar
 */
@Composable
fun AddCustomDhikrDialog(
    onDismiss: () -> Unit,
    onConfirm: (arabic: String, transliteration: String, translated: String, target: Int) -> Unit
) {
    var arabic by remember { mutableStateOf("") }
    var transliteration by remember { mutableStateOf("") }
    var translated by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("33") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        title = {
            Text(
                text = "Add Custom Dhikr",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(dimens.space8)) {
                OutlinedTextField(
                    value = transliteration,
                    onValueChange = { transliteration = it },
                    label = { Text("Name / Transliteration (e.g. Astaghfirullah)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = arabic,
                    onValueChange = { arabic = it },
                    label = { Text("Arabic Text (Optional)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = target,
                    onValueChange = { target = it },
                    label = { Text("Target Count (e.g. 33, 100)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (transliteration.isNotBlank()) {
                        val tInt = target.toIntOrNull() ?: 33
                        onConfirm(arabic, transliteration, translated, tInt)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
