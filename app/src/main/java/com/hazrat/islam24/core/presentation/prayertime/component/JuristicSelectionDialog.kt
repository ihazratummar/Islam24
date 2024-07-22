package com.hazrat.islam24.core.presentation.prayertime.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.JuristicMethodDetails
import com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.schoolDetailsList
import com.hazrat.islam24.ui.theme.dimens

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun JuristicSelectionDialog(
    showJuristicSelectionDialog: Boolean,
    onJuristicSelected: (JuristicMethodDetails) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheetLayout(
        sheetContent = {
            Surface(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size20)
                    .nestedScroll(rememberNestedScrollInteropConnection()),
                color = Color.Transparent
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = MaterialTheme.dimens.size10,
                                vertical = MaterialTheme.dimens.size10
                            )
                    ) {
                        Text(
                            text = "Select School of thought for changing the Asr Time",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium
                        )

                    }
                    schoolDetailsList.forEach { index ->
                        val school = schoolDetailsList[index.number]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.size10)
                                .clickable {
                                    onJuristicSelected(school)
                                    onDismiss()
                                },
                            colors = CardDefaults.cardColors(Color.Transparent),
                            shape = RoundedCornerShape(MaterialTheme.dimens.size50)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(MaterialTheme.dimens.size12),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = school.name,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
                                )
                            }
                            Divider(color = MaterialTheme.colorScheme.inverseOnSurface)
                        }
                    }
                }
            }
        },
        sheetState = rememberModalBottomSheetState(
            initialValue = if (showJuristicSelectionDialog) ModalBottomSheetValue.Expanded else ModalBottomSheetValue.Hidden
        ),
        sheetShape = MaterialTheme.shapes.large,
        sheetBackgroundColor = MaterialTheme.colorScheme.surfaceContainerLow ,
        sheetContentColor = MaterialTheme.colorScheme.surface
    ) {

    }

}

