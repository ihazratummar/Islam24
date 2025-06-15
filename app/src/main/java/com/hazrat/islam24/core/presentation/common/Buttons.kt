package com.hazrat.islam24.core.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.ui.theme.Islam24Theme
import com.hazrat.ui.theme.dimens

@Composable
fun ContinueButton(
    buttonText: String,
    onClick: () -> Unit

) {
    OutlinedButton(modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(size = dimens.size6)
    ) {
        Text(text = buttonText,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun ContinueButtonPreview(){
    Islam24Theme {
        ContinueButton(buttonText = "Continue", onClick = {}
        )
    }
}
