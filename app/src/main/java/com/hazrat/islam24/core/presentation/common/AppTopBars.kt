package com.hazrat.islam24.core.presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens


/**
 * @author Hazrat Ummar Shaikh
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBar(
    modifier: Modifier = Modifier,
    topBarTitle: String = "",
    onBackClick: () -> Unit,
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.padding(dimens.size10),
            onClick = { onBackClick.invoke() },
        ) {
            Icon(
                painterResource(R.drawable.backicon),
                contentDescription = null,
                tint = iconColor
            )
        }
        Text(
            text = topBarTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
