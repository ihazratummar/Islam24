package com.hazrat.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hazrat.ui.theme.dimens

@Composable
fun ContinueButton(
    buttonText: String,
    onClick: () -> Unit

) {
    OutlinedButton(modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(size = dimens.space8)
    ) {
        Text(text = buttonText,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}
