package com.hazrat.islam24.core.presentation.prayertime.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.SchoolDetails
import com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.schoolDetailsList
import com.hazrat.islam24.ui.theme.dimens

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
                    .padding(MaterialTheme.dimens.size20)
                    .nestedScroll(rememberNestedScrollInteropConnection()),
                color = Color.Transparent
            ) {
                LazyColumn {
                    item { 
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.size60)
                                .padding(
                                    horizontal = MaterialTheme.dimens.size10,
                                    vertical = MaterialTheme.dimens.size10
                                )
                        ){
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Select School of thought for changing the Asr Time",
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                    items(schoolDetailsList.size){ index ->
                        val school = schoolDetailsList[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.size10)
                                .clickable {
                                    onSchoolSelected(school)
                                    onDismiss()
                                },
                            colors = CardDefaults.cardColors(Color.Transparent),
                            border = BorderStroke(MaterialTheme.dimens.size1, Color.Green)
                        ) {
                            Row(modifier = Modifier
                                .fillMaxSize()
                                .padding(MaterialTheme.dimens.size12),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = school.name,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
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
        sheetBackgroundColor = Color(0xff031600),
    ) {

    }
}