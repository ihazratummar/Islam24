package com.hazrat.islam24.core.presentation.tasbih.tasbihcomponent

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.islam24.R
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens

@Composable
fun RepeatCountDialog(
    onDismiss: () -> Unit,
    onRepeatCountSelected: (Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    var selectedRepeatCount by remember { mutableIntStateOf(33) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .padding(MaterialTheme.dimens.size15)
                .width(MaterialTheme.dimens.size300),
            shape = RoundedCornerShape(MaterialTheme.dimens.size20),
            color = MaterialTheme.colorScheme.background,
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
                    text = stringResource(R.string.set_tasbih_count),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = MaterialTheme.dimens.size15),
                    color = colorResource(id = R.color.text)
                )
                // Display the selected value
                Text(
                    text = selectedRepeatCount.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.text)
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
                            contentColor = colorResource(id = R.color.text),
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = colorResource(id = R.color.text).copy(0.1f)
                        ),
                        border = BorderStroke(MaterialTheme.dimens.size1, color = Color.Green)
                    ) {
                        Text(stringResource(R.string.cancel), style = MaterialTheme.typography.labelMedium)
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
                            contentColor = colorResource(id = R.color.text),
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = colorResource(id = R.color.text).copy(0.1f)
                        ),
                        border = BorderStroke(MaterialTheme.dimens.size1, color = colorResource(id = R.color.primary))
                    ) {
                        Text(stringResource(R.string.select), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}