package com.hazrat.islam24.core.presentation.prayertime.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.hazrat.islam24.R
import com.hazrat.islam24.ui.theme.dimens


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MethodSelectionDialog(
    showMethodSelectionDialog: Boolean,
    onMethodSelected: (com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheetLayout(
        sheetContent = {
            Surface(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.size20)
                    .nestedScroll(rememberNestedScrollInteropConnection()),
                color = Color.Transparent
            ) {
                LazyColumn{
                    item{
                        Text(
                            text = stringResource(id = R.string.prayer_times),
                            style = TextStyle(fontSize = 10.sp),
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier.padding(top = MaterialTheme.dimens.size30)
                        )
                    }
                    items(com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.prayerMethods.size) { index ->
                        val method = com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.prayerMethods[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.size60)
                                .padding(vertical = MaterialTheme.dimens.size5)
                                .clickable {
                                    onMethodSelected(method)
                                    onDismiss()
                                },
                            colors = CardDefaults.cardColors(Color.Transparent),
                            shape = RoundedCornerShape(MaterialTheme.dimens.size50)
                        ) {
                            Row(modifier = Modifier
                                .fillMaxSize()
                                .padding(MaterialTheme.dimens.size10),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = method.name,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxHeight(),
        sheetState = rememberModalBottomSheetState(
            initialValue = if (showMethodSelectionDialog) ModalBottomSheetValue.Expanded else ModalBottomSheetValue.Hidden
        ),
        sheetShape = MaterialTheme.shapes.large,
        sheetBackgroundColor = Color(0xff031600) ,
        sheetContentColor = MaterialTheme.colorScheme.surface
    ) {
        // Empty content
    }
}
