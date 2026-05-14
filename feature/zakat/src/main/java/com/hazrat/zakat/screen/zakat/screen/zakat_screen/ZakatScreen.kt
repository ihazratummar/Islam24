package com.hazrat.zakat.screen.zakat.screen.zakat_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hazrat.common.BasicTopBarWithAction
import com.hazrat.ui.R
import com.hazrat.ui.animations.LottieAnimation
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil.getDateFromLong

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatScreen(
    modifier: Modifier = Modifier,
    zakatScreenState: ZakatScreenState,
    zakatScreenEvent: (ZakatScreenEvent) -> Unit,
    onNewAddClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    getZakatDetails: (String) -> Unit

) {


    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.space48))
            BasicTopBarWithAction(
                onBackClick = {
                    onBackClick.invoke()
                },
                actionIcon = painterResource(R.drawable.menu_01),
                onActionClick = {
                    zakatScreenEvent(ZakatScreenEvent.ToggleSortType)
                }

            )
            LazyColumn(
                modifier = modifier
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(dimens.space12)
                            .fillMaxWidth()
                            .clickable {
                                onNewAddClick()
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimens.compTopBar)
                                .padding(dimens.space16),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.start_calculation),
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }
                item {
                    if (zakatScreenState.zakatEntity.isNotEmpty()) {
                        zakatScreenState.zakatEntity.forEach {
                            Card(
                                modifier = Modifier
                                    .padding(dimens.space12)
                                    .fillMaxWidth()
                                    .clickable {
                                        getZakatDetails(it.id)
                                        zakatScreenEvent(ZakatScreenEvent.ToggleZakatDetailsPopUp)
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.padding(dimens.space12)
                                    ) {
                                        Text(
                                            text = getDateFromLong(it.date),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = stringResource(
                                                R.string.total_assets,
                                                it.totalAsset
                                            ),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = stringResource(
                                                R.string.zakat_amount,
                                                it.zakatAmount
                                            ),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            zakatScreenEvent(
                                                ZakatScreenEvent.DeleteZakat(
                                                    zakatId = it.id
                                                )
                                            )
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete"
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        LottieAnimation(
                            modifier = Modifier,
                            lottieJson = R.raw.emptylist
                        )
                    }
                }

            }
        }

        if (zakatScreenState.isZakatDetailsOpen){

            val sheet = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.background,
                sheetState = sheet,
                onDismissRequest = {zakatScreenEvent(ZakatScreenEvent.ToggleZakatDetailsPopUp)}
            ) {
                ZakatDetails(
                    zakatState = zakatScreenState
                )
            }
        }
    }

}