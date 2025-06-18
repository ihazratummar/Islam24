package com.hazrat.islam24.core.presentation.prayertime.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.hazrat.ui.R
import com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.CalculationMethodDetails
import com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.prayerMethods
import com.hazrat.ui.theme.dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerCalculationDialog(
    onMethodSelected: (CalculationMethodDetails) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight(),
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = dimens.size20)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.prayer_times),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = dimens.size30)
                )
            }
            item {
                Spacer(modifier = Modifier.height(dimens.size10))
            }
            items(prayerMethods) { methods ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.size15)
                        .clickable {
                            onMethodSelected(methods)
                            onDismiss()
                        },
                    colors = CardDefaults.cardColors(Color.Transparent),
                    shape = RoundedCornerShape(dimens.size50)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimens.size10),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = methods.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = dimens.size10)
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.inverseOnSurface)
                }
            }
        }
    }
}
