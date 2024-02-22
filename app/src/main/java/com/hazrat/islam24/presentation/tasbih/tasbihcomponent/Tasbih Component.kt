package com.hazrat.islam24.presentation.tasbih.tasbihcomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.islam24.presentation.tasbih.TasbihViewModel
import com.hazrat.islam24.ui.theme.Hidayat
import com.hazrat.islam24.ui.theme.dimens

@Composable
fun RepeatCountDialog(
    onDismiss: () -> Unit,
    onRepeatCountSelected: (Int) -> Unit,
    viewModel: TasbihViewModel = hiltViewModel()
) {
    var selectedRepeatCount by remember { mutableIntStateOf(33) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .padding(MaterialTheme.dimens.size15)
                .width(MaterialTheme.dimens.size300),
            shape = RoundedCornerShape(MaterialTheme.dimens.size20),
            color = Color(0xFF0B220E),
            border = BorderStroke(MaterialTheme.dimens.size1,
                color = Color.Green.copy(0.5f))
        ) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size15),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Set Tasbih Count",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = MaterialTheme.dimens.size15),
                    fontFamily = Hidayat,
                    color = Color.White
                )
                // Display the selected value
                Text(
                    text = selectedRepeatCount.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = Hidayat,
                    color = Color.White
                )

                Slider(
                    value = selectedRepeatCount.toFloat(),
                    onValueChange = { newValue ->
                        selectedRepeatCount = newValue.toInt()
                    },
                    valueRange = 1f..300f,
                    steps = 300,
                    modifier = Modifier.padding(bottom = MaterialTheme.dimens.size15)
                )
                Row {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(topStart = MaterialTheme.dimens.size10, bottomEnd = MaterialTheme.dimens.size10),
                        colors = ButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Black
                        ),
                        border = BorderStroke(MaterialTheme.dimens.size1, color = Color.Green)
                    ) {
                        Text("Cancel", fontFamily = Hidayat)
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.size10))
                    Button(
                        onClick = {
                            onRepeatCountSelected(selectedRepeatCount)
                            onDismiss()
                            viewModel.resetTasbihCount()

                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(topStart = MaterialTheme.dimens.size10, bottomEnd = MaterialTheme.dimens.size10),
                        colors = ButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Black
                        ),
                        border = BorderStroke(MaterialTheme.dimens.size1, color = Color.Green)
                    ) {
                        Text("Select", fontFamily = Hidayat)
                    }
                }
            }
        }
    }
}