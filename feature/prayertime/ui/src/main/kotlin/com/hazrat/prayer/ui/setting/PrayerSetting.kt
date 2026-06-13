package com.hazrat.prayer.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.prayersettingmodel.JuristicMethod
import com.hazrat.model.prayersettingmodel.prayerMethods
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.TopAppBarTitle
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerSetting(
    onBackClick: () -> Unit,
    state: PrayerSettingState,
    event: (PrayerSettingEvent) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TopAppBarTitle(R.string.prayer_setting)
                },
                navigationIcon = {
                    BackIcon(
                        onBackClick = onBackClick
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                windowInsets = WindowInsets(top = dimens.space20)
            )
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->

        if (state.isRefresh) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }


        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.space20),
            verticalArrangement = Arrangement.spacedBy(dimens.space20)
        ) {
            item {

                Text(
                    text = "CALCULATION METHOD",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = customColors.secondaryText,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimens.cornerMd),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    prayerMethods.forEachIndexed { index, methodDetails ->
                        val isSelected = state.calculationMethod == methodDetails.method
                        val lastIndex = index == prayerMethods.size - 1
                        Row(
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        if (!isSelected) {
                                            event(PrayerSettingEvent.CalculationChanged(value = methodDetails.method))
                                        }
                                    }
                                )
                                .padding(vertical = dimens.space8, horizontal = dimens.space16)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(dimens.iconSm)
                                    .let {
                                        if (isSelected) it.background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = CircleShape
                                        ) else it.border(
                                            width = dimens.space2,
                                            color = customColors.secondaryText,
                                            shape = CircleShape
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(dimens.iconSx)
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.spacedBy(dimens.space4)
                            ) {
                                Text(
                                    text = methodDetails.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${methodDetails.farjAngle}/${methodDetails.ishaAngle} ● ${methodDetails.region}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = customColors.secondaryText
                                    )
                                )
                            }
                        }
                        if (!lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }

            item {
                Text(
                    text = "JURUSTIC METHOD (ASR)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = customColors.secondaryText,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }


            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimens.cornerMd),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    JuristicMethod.entries.forEachIndexed { index, juristicDetails ->
                        val isSelected = state.juristic == juristicDetails.index
                        val lastIndex = index == JuristicMethod.entries.size - 1
                        Row(
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        if (!isSelected){
                                            event(PrayerSettingEvent.JuristicChanged(value = juristicDetails.index))
                                        }
                                    }
                                )
                                .padding(vertical = dimens.space8, horizontal = dimens.space16)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(dimens.iconSm)
                                    .let {
                                        if (isSelected) it.background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = CircleShape
                                        ) else it.border(
                                            width = dimens.space2,
                                            color = customColors.secondaryText,
                                            shape = CircleShape
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(dimens.iconSx)
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.spacedBy(dimens.space4)
                            ) {
                                Text(
                                    text = juristicDetails.toString(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = juristicDetails.description,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = customColors.secondaryText
                                    )
                                )
                            }
                        }
                        if (!lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }


//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            val calculationMethod = prayerMethods[state.calculationMethod]
//            PrayerSettingCard(
//                text = stringResource(R.string.calculation_method),
//                methodID = calculationMethod.name,
//                method = "${calculationMethod.farjAngle}°/${calculationMethod.ishaAngle}° ● ${calculationMethod.region}",
//                onClick = {
//                    event(PrayerSettingEvent.OpenCalculationDialog)
//                }
//            )
//            PrayerSettingCard(
//                text = stringResource(R.string.juristic_method),
//                methodID = JuristicMethod.entries[state.juristic].name,
//                method = null,
//                onClick = {
//                    event(PrayerSettingEvent.OpenJuristicDialog)
//                }
//            )
//        }
//        if (state.isCalculationDialogOpen) {
//            PrayerCalculationDialog(
//                onMethodSelected = { method ->
//                    event(PrayerSettingEvent.CalculationChanged(method.method))
//                },
//                onDismiss = { event(PrayerSettingEvent.OpenCalculationDialog) }
//            )
//        }
//        if (state.isJuristicDialogOpen) {
//            JuristicSelectionDialog(
//                onJuristicSelected = { juristic ->
//                    event(PrayerSettingEvent.JuristicChanged(juristic.number))
//                },
//                onDismiss = { event(PrayerSettingEvent.OpenJuristicDialog) }
//            )
//        }
//
//    }

}
