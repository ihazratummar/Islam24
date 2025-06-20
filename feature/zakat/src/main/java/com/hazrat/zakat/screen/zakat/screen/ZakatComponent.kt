package com.hazrat.zakat.screen.zakat.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hazrat.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 18-06-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetsInfoModal(
    modifier: Modifier = Modifier,
    title: Int,
    onDismiss: () -> Unit = {},
    keyPoints: Int? = null,
    infoText: List<Int>,
    bulletPointText: List<Int?> = emptyList(),
    conclusionText: Int? = null
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = title))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.size15)
            ) {
                items (infoText) {text ->
                    Text(
                        text = stringResource(text),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = dimens.size15)
                    )
                }
                item{
                    keyPoints?.let {text ->
                        Text(
                            text = stringResource(text),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = dimens.size8)
                        )
                    }
                }
                items(bulletPointText) { text ->
                    text?.let {
                        BulletPoint(text = stringResource(text))
                    }
                }
                item{
                    conclusionText?.let {
                        Text(
                            text = stringResource(it),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = dimens.size15)
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun BulletPoint(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
