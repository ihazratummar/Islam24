package com.hazrat.prayer.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import com.hazrat.model.prayersettingmodel.JuristicMethodDetails
import com.hazrat.model.prayersettingmodel.schoolDetailsList
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuristicSelectionDialog(
    onJuristicSelected: (JuristicMethodDetails) -> Unit,
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
        Surface(
            modifier = Modifier
                .padding(dimens.space20)
                .nestedScroll(rememberNestedScrollInteropConnection()),
            color = Color.Transparent
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimens.space12,
                            vertical = dimens.space12
                        )
                ) {
                    Text(
                        text = stringResource(R.string.determining_the_time_of_asr_the_method_of_the_jurists),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )

                }
                schoolDetailsList.forEach { index ->
                    val school = schoolDetailsList[index.number]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimens.space16)
                            .clickable {
                                onJuristicSelected(school)
                                onDismiss()
                            },
                        colors = CardDefaults.cardColors(Color.Transparent),
                        shape = RoundedCornerShape(dimens.cornerFull)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(dimens.space12),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = school.name,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = dimens.space12)
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceDim)
                    }
                }
            }
        }
    }

}

