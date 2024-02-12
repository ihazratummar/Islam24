package com.hazrat.islam24.presentation.prayertime.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.domain.model.prayertime.prayersettingmodel.SchoolDetails
import com.hazrat.islam24.domain.model.prayertime.prayersettingmodel.schoolDetailsList
import com.hazrat.islam24.presentation.Dimens.Size10
import com.hazrat.islam24.presentation.Dimens.Size12
import com.hazrat.islam24.presentation.Dimens.Size4
import com.hazrat.islam24.presentation.Dimens.Size40

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SchoolSelectionDialog(
    showSchoolSelectionDialog: Boolean,
    onSchoolSelected: (SchoolDetails) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheetLayout(
        sheetContent = {
            Surface(
                modifier = Modifier
                    .padding(Size40)
                    .nestedScroll(rememberNestedScrollInteropConnection()),
                color = Color.Transparent
            ) {
                LazyColumn {
                    items(schoolDetailsList.size){ index ->
                        val school = schoolDetailsList[index]
                        Card(
                            modifier = Modifier.
                            fillMaxWidth()
                                .padding(Size4)
                                .clickable {
                                    onSchoolSelected(school)
                                    onDismiss()
                                }
                        ) {
                            Row(modifier = Modifier
                                .fillMaxSize()
                                .padding(Size12),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = school.name,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = Size10)
                                )
                            }
                        }
                    }
                }
            }
        },
        sheetState = rememberModalBottomSheetState(
            initialValue = if (showSchoolSelectionDialog) ModalBottomSheetValue.Expanded else ModalBottomSheetValue.Hidden
        ),
        sheetShape = MaterialTheme.shapes.large,
        sheetBackgroundColor = MaterialTheme.colorScheme.onBackground,
        sheetContentColor = MaterialTheme.colorScheme.surface
    ) {

    }
}