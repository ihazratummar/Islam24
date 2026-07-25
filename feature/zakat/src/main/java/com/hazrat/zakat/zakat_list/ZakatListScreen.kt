package com.hazrat.zakat.zakat_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.database.entity.zakat.ZakatEntity
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.TopAppBarTitle
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil.getDateFromLong
import com.hazrat.utils.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatListScreen(
    modifier: Modifier = Modifier,
    uiState: ZakatListState,
    onEvent: (ZakatListEvent) -> Unit,
    onNewAddClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    getZakatDetails: (String) -> Unit = {}
) {
    var itemToDelete by remember { mutableStateOf<ZakatEntity?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        TopAppBarTitle(title = "Zakat History")
                        Text(
                            text = "Saved Statements",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    BackIcon(onBackClick = onBackClick)
                },
                actions = {
                    IconButton(
                        onClick = onNewAddClick,
                        modifier = Modifier
                            .size(dimens.space40)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Add New Zakat",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(dimens.cornerLg),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "New Zakat",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                },
                text = {
                    Text(
                        text = "Calculate Zakat",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.zakatEntityList.isEmpty()) {
                // Centered Empty State without Card and without Button
                EmptyZakatHistoryView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimens.space20)
                ) {
                    // Summary Overview Banner
                    item {
                        val totalSavedZakat = uiState.zakatEntityList.sumOf { it.zakatAmount }
                        val statementCount = uiState.zakatEntityList.size
                        Spacer(modifier = Modifier.height(dimens.space12))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(dimens.cornerXl),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimens.space20),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Total Zakat Tracked ($statementCount Statements)",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                    Spacer(modifier = Modifier.height(dimens.space4))
                                    Text(
                                        text = formatCurrency(totalSavedZakat),
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(dimens.space48)
                                        .clip(RoundedCornerShape(dimens.cornerMd))
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.zakat),
                                        contentDescription = "Zakat",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(dimens.iconLg)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(dimens.space16))
                    }

                    // History Item Cards
                    items(uiState.zakatEntityList) { item ->
                        ZakatHistoryCard(
                            item = item,
                            onDeleteClick = { itemToDelete = item },
                            onCardClick = {
                                getZakatDetails(item.id)
                                onEvent(ZakatListEvent.ToggleZakatDetailsPopUp)
                            }
                        )
                        Spacer(modifier = Modifier.height(dimens.space16))
                    }
                }
            }
        }

        // Delete Confirmation Modal Dialog
        itemToDelete?.let { target ->
            AlertDialog(
                onDismissRequest = { itemToDelete = null },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                title = {
                    Text(
                        text = "Delete Zakat Record?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete the Zakat statement recorded on ${getDateFromLong(target.date)}? This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onEvent(ZakatListEvent.DeleteZakat(target.id))
                            itemToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        shape = RoundedCornerShape(dimens.cornerLg)
                    ) {
                        Text(text = "Delete", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { itemToDelete = null }) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }

        // Details Bottom Sheet
        if (uiState.isZakatDetailsOpen) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                sheetState = sheetState,
                onDismissRequest = {
                    onEvent(ZakatListEvent.ToggleZakatDetailsPopUp)
                }
            ) {
                ZakatDetailsBottomSheet(uiState = uiState)
            }
        }
    }
}

@Composable
private fun EmptyZakatHistoryView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.space32),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.space64)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.zakat),
                    contentDescription = "No Zakat",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimens.iconLg)
                )
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            Text(
                text = "No Zakat Records Yet",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(dimens.space8))

            Text(
                text = "Track your yearly Zakat obligations. Tap the + button to calculate and save your statement.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = dimens.space12)
            )
        }
    }
}

@Composable
private fun ZakatHistoryCard(
    item: ZakatEntity,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                dimens.divider,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(dimens.cornerXl)
            )
            .clip(RoundedCornerShape(dimens.cornerXl))
            .clickable { onCardClick() },
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                    Spacer(modifier = Modifier.width(dimens.space8))
                    Text(
                        text = getDateFromLong(item.date),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(dimens.space32)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = "Delete Record",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(dimens.iconXs)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space12))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = dimens.divider
            )
            Spacer(modifier = Modifier.height(dimens.space12))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(dimens.space40)
                            .clip(RoundedCornerShape(dimens.cornerMd))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.zakat),
                            contentDescription = "Zakat Record",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(dimens.iconMd)
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.space12))

                    Column {
                        Text(
                            text = formatCurrency(item.totalAsset),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Net Wealth",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatCurrency(item.zakatAmount),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = customColors.accentColor
                    )
                    Text(
                        text = "Zakat Due",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tap to view breakdown ›",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
