package com.hazrat.islam24.presentation.tasbih.tasbihcomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun RepeatCountDialog(
    onDismiss: () -> Unit,
    onRepeatCountSelected: (Int) -> Unit
) {
    var selectedRepeatCount by remember { mutableIntStateOf(33) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Set Tasbih Count",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // Display the selected value
                Text(
                    text = selectedRepeatCount.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )

                Slider(
                    value = selectedRepeatCount.toFloat(),
                    onValueChange = { newValue ->
                        selectedRepeatCount = newValue.toInt()
                    },
                    valueRange = 1f..300f,
                    steps = 300,
                    modifier = Modifier.padding(bottom = 16.dp)
                )



                Button(
                    onClick = {
                        onRepeatCountSelected(selectedRepeatCount)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select")
                }
            }
        }
    }
}