package com.hazrat.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-06-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBarWithAction(
    modifier: Modifier = Modifier,
    topBarTitle: String = "",
    onBackClick: () -> Unit,
    actionIcon: Painter,
    onActionClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBackClick.invoke() }
        ) {
            Icon(
                painterResource(R.drawable.backicon), contentDescription = null,
                modifier = Modifier.size(dimens.size20)
            )
        }
        Text(
            text = topBarTitle,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            modifier = Modifier.padding(dimens.size10),
            onClick = { onActionClick.invoke() }
        ) {
            Icon(
                modifier = Modifier.size(dimens.size30),
                painter = actionIcon,
                contentDescription = null
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithTwoAction(
    modifier: Modifier = Modifier,
    topBarTitle: String = "",
    onBackClick: () -> Unit,
    firstActionIcon: Painter,
    isFirstActionEnabled: Boolean = true,
    isSecondActionEnabled: Boolean = true,
    onFirstActionClick: () -> Unit,
    secondActionIcon: Painter,
    onSecondActionClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBackClick.invoke() }
        ) {
            Icon(painterResource(R.drawable.backicon), contentDescription = null)
        }
        Text(text = topBarTitle)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onFirstActionClick.invoke() },
            enabled = isFirstActionEnabled,
        ) {
            Icon(
                firstActionIcon,
                contentDescription = null,
                tint = if (isFirstActionEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
        }
        IconButton(
            onClick = { onSecondActionClick.invoke() },
            enabled = isSecondActionEnabled
        ) {
            Icon(secondActionIcon, contentDescription = null)
        }
    }
}


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
