package com.hazrat.islam24.core.presentation.zakat.screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.common.BasicTopBarWithAction
import com.hazrat.islam24.core.presentation.zakat.ZakatEvent
import com.hazrat.islam24.core.presentation.zakat.ZakatState
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.DateUtil.getDateFromLong

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatScreen(
    modifier: Modifier = Modifier,
    zakatState: ZakatState,
    zakatEvent: (ZakatEvent) -> Unit,
    onNewAddClick: () -> Unit = {},
    openZakat: (String) -> Unit = {},
    onBackClick: () -> Unit = {}

) {


    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicTopBarWithAction(
                onBackClick = {
                    onBackClick.invoke()
                },
                actionIcon = painterResource(R.drawable.menu_01),
                onActionClick = {
                    zakatEvent(ZakatEvent.ToggleSortType)
                }

            )
            LazyColumn(
                modifier = modifier
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
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
                                .height(65.dp)
                                .padding(15.dp),
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
                    if (zakatState.zakatEntity.isNotEmpty()) {
                        zakatState.zakatEntity.forEach {
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        openZakat(it.id)
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
                                        modifier = Modifier.padding(10.dp)
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
                                            zakatEvent(
                                                ZakatEvent.DeleteZakat(
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
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "No data found")
                        }
                    }
                }

            }
        }
    }

}