package com.hazrat.zakat.screen.zakat.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.hazrat.common.TopBarWithTwoAction
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import com.hazrat.zakat.screen.zakat.ZakatEvent
import com.hazrat.zakat.screen.zakat.ZakatState
import com.hazrat.zakat.screen.zakat.screen.info.goldInfoText
import com.hazrat.zakat.screen.zakat.screen.info.moneyInfoBullet
import com.hazrat.zakat.screen.zakat.screen.info.moneyInfoText
import com.hazrat.zakat.screen.zakat.screen.info.monthlyCostBullet

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationScreen(
    modifier: Modifier = Modifier,
    zakatState: ZakatState,
    zakatEvent: (ZakatEvent) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val localResource = LocalResources.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(topEnd = dimens.cornerXl, topStart = dimens.cornerXl)
                    ),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center
                ) {
                    BottomBarItem(
                        text = stringResource(R.string.nisab),
                        amount = zakatState.nisabAmount
                    )
                    BottomBarItem(
                        text = stringResource(R.string.total_asset1),
                        amount = "${zakatState.totalAsset}"
                    )
                    BottomBarItem(
                        text = stringResource(R.string.zakat_amount1),
                        amount = "${zakatState.zakatAmount}"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopBarWithTwoAction(
                topBarTitle = stringResource(R.string.zakat_calculator),
                onBackClick = { onBackClick.invoke() },
                firstActionIcon = painterResource(R.drawable.save),
                secondActionIcon = painterResource(R.drawable.refresh),
                isFirstActionEnabled = zakatState.money != "0.0" || zakatState.gold != "0.0" ||
                        zakatState.silver != "0.0" || zakatState.tradeAmount != "0.0"
                        || zakatState.monthCost != "0.0" || zakatState.debt != "0.0",
                onFirstActionClick = {
                    zakatEvent(ZakatEvent.SaveZakat)
                    onSaveClick()
                    zakatEvent(ZakatEvent.ResetAllState)
                    Toast.makeText(
                        context,
                        localResource.getString(R.string.done), Toast.LENGTH_SHORT
                    ).show()
                },
                onSecondActionClick = {
                    zakatEvent(ZakatEvent.ResetAllState)
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimens.space12)
            ) {
                item {
                    CalculateItems(
                        text = stringResource(R.string.money),
                        amount = zakatState.money,
                        onClick = { zakatEvent(ZakatEvent.OpenMoneyDialog) },
                        infoNavigation = {
                            zakatEvent(ZakatEvent.ToggleMoneyInfoDialog)
                        }
                    )
                    CalculateItems(
                        text = stringResource(R.string.gold),
                        amount = zakatState.gold,
                        onClick = { zakatEvent(ZakatEvent.OpenGoldDialog) },
                        infoNavigation = { zakatEvent(ZakatEvent.ToggleGoldInfoDialog) }
                    )
                    CalculateItems(
                        text = stringResource(R.string.silver),
                        amount = zakatState.silver,
                        onClick = { zakatEvent(ZakatEvent.OpenSilverDialog) },
                        infoNavigation = { zakatEvent(ZakatEvent.ToggleSilverInfoDialog) }
                    )
                    CalculateItems(
                        text = stringResource(R.string.trade_amount),
                        amount = zakatState.tradeAmount,
                        onClick = { zakatEvent(ZakatEvent.OpenTradeAmountDialog) }
                    )
                    CalculateItems(
                        text = stringResource(R.string.monthly_living_cost),
                        amount = zakatState.monthCost,
                        onClick = { zakatEvent(ZakatEvent.OpenMonthCostDialog) },
                        infoNavigation = { zakatEvent(ZakatEvent.ToggleMonthlyInfoDialog) },
                        fontColor = MaterialTheme.colorScheme.error
                    )
                    CalculateItems(
                        text = stringResource(R.string.total_debt_on_you),
                        amount = zakatState.debt,
                        onClick = { zakatEvent(ZakatEvent.OpenDebtDialog) },
                        infoNavigation = { zakatEvent(ZakatEvent.ToggleTotalDebtInfoDialog) },
                        fontColor = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        if (zakatState.isGoldInfoDialogOpen) {
            AssetsInfoModal(
                title = R.string.gold,
                infoText = goldInfoText,
                onDismiss = {
                    zakatEvent(ZakatEvent.ToggleGoldInfoDialog)
                }
            )
        }

        if (zakatState.isSilverInfoDialogOpen) {
            AssetsInfoModal(
                title = R.string.silver,
                infoText = listOf(R.string.silverinfo),
                onDismiss = {
                    zakatEvent(ZakatEvent.ToggleSilverInfoDialog)
                }
            )
        }

        if (zakatState.isMoneyInfoDialogOpen) {
            AssetsInfoModal(
                title = R.string.money,
                infoText = moneyInfoText,
                bulletPointText = moneyInfoBullet,
                conclusionText = R.string.moneyinfo1,
                onDismiss = {
                    zakatEvent(ZakatEvent.ToggleMoneyInfoDialog)
                }
            )
        }

        if (zakatState.isMonthCostInfoDialogOpen) {
            AssetsInfoModal(
                title = R.string.monthly_living_cost,
                infoText = listOf(R.string.monthlyinfo),
                keyPoints = R.string.key_points_to_consider,
                bulletPointText = monthlyCostBullet,
                conclusionText = R.string.note_exclude_luxury_expenses_such_as_vacations_luxury_goods_or_excessive_entertainment,
                onDismiss = {
                    zakatEvent(ZakatEvent.ToggleMonthlyInfoDialog)
                }
            )
        }
        if (zakatState.isDebtInfoDialogOpen) {
            AssetsInfoModal(
                title = R.string.debt,
                infoText = listOf(R.string.debtinfo, R.string.debtinfo1),
                onDismiss = {
                    zakatEvent(ZakatEvent.ToggleTotalDebtInfoDialog)
                }
            )
        }
    }

    if (zakatState.isMoneyDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { zakatEvent(ZakatEvent.OpenMoneyDialog) },
            value = zakatState.money,
            onValueChange = {
                zakatEvent(ZakatEvent.OnMoneyValueChange(it))

            },
            onClick = {
                zakatEvent(ZakatEvent.OnMoneySubmit(zakatState.money))
                zakatEvent(ZakatEvent.OpenMoneyDialog)
            },
            onDone = {
                zakatEvent(ZakatEvent.OnMoneySubmit(zakatState.money))
                zakatEvent(ZakatEvent.OpenMoneyDialog)
            },
            title = "Enter Total Cash or Bank Savings"
        )
    }

    if (zakatState.isGoldDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { zakatEvent(ZakatEvent.OpenGoldDialog) },
            value = zakatState.gold,
            onValueChange = {
                zakatEvent(ZakatEvent.OnGoldPriceChange(it))
            },
            onClick = {
                zakatEvent(ZakatEvent.OnSubmitGold(zakatState.gold))
                zakatEvent(ZakatEvent.OpenGoldDialog)
            },
            onDone = {
                zakatEvent(ZakatEvent.OnSubmitGold(zakatState.gold))
                zakatEvent(ZakatEvent.OpenGoldDialog)
            },
            title = "Enter Total Gold Value"
        )
    }

    if (zakatState.isSilverDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { zakatEvent(ZakatEvent.OpenSilverDialog) },
            value = zakatState.silver,
            onValueChange = {
                zakatEvent(ZakatEvent.OnSilverPriceChange(it))
            },
            onClick = {
                zakatEvent(ZakatEvent.OnSubmitSilver(zakatState.silver))
                zakatEvent(ZakatEvent.OpenSilverDialog)
            },
            onDone = {
                zakatEvent(ZakatEvent.OnSubmitSilver(zakatState.silver))
                zakatEvent(ZakatEvent.OpenSilverDialog)
            },
            title = "Enter Total Silver Value"
        )
    }
    if (zakatState.isTradeAmountDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { zakatEvent(ZakatEvent.OpenTradeAmountDialog) },
            value = zakatState.tradeAmount,
            onValueChange = {
                zakatEvent(ZakatEvent.OnTradeAmountPriceChange(it))
            },
            onClick = {
                zakatEvent(ZakatEvent.OnSubmitTradeAmount(zakatState.tradeAmount))
                zakatEvent(ZakatEvent.OpenTradeAmountDialog)
            },
            onDone = {
                zakatEvent(ZakatEvent.OnSubmitTradeAmount(zakatState.tradeAmount))
                zakatEvent(ZakatEvent.OpenTradeAmountDialog)
            },
            title = "Enter Value of Business Inventory"
        )
    }
    if (zakatState.isMonthCostDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { zakatEvent(ZakatEvent.OpenMonthCostDialog) },
            value = zakatState.monthCost,
            onValueChange = {
                zakatEvent(ZakatEvent.OnMonthCostPriceChange(it))
            },
            onClick = {
                zakatEvent(ZakatEvent.OnSubmitMonthCost(zakatState.monthCost))
                zakatEvent(ZakatEvent.OpenMonthCostDialog)
            },
            onDone = {
                zakatEvent(ZakatEvent.OnSubmitMonthCost(zakatState.monthCost))
                zakatEvent(ZakatEvent.OpenMonthCostDialog)
            },
            title = "Enter Total Monthly Expenses"
        )
    }
    if (zakatState.isDebtDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { zakatEvent(ZakatEvent.OpenDebtDialog) },
            value = zakatState.debt,
            onValueChange = {
                zakatEvent(ZakatEvent.OnDebtPriceChange(it))
            },
            onClick = {
                zakatEvent(ZakatEvent.OnSubmitDebt(zakatState.debt))
                zakatEvent(ZakatEvent.OpenDebtDialog)
            },
            onDone = {
                zakatEvent(ZakatEvent.OnSubmitDebt(zakatState.debt))
                zakatEvent(ZakatEvent.OpenDebtDialog)
            },
            title =  "Enter Outstanding Debts"
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CalculationItemDialogs(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onClick: () -> Unit,
    onDismiss: () -> Unit = {},
    onDone: () -> Unit = {},
    title: String = ""
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        modifier = Modifier.imePadding(),
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                )
            )
            Spacer(modifier = Modifier.height(dimens.space16))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onDone() }
                )
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.space12),
                onClick = onClick,
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Add",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
fun CalculateItems(
    text: String,
    amount: String = "0.0",
    onClick: () -> Unit = {},
    infoNavigation: () -> Unit = {},
    fontColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.space4)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.space12, vertical = dimens.space12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontWeight = FontWeight.SemiBold)
            IconButton(onClick = { infoNavigation() }) {
                Icon(painter = painterResource(R.drawable.alert), contentDescription = "Information")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = amount, fontWeight = FontWeight.SemiBold, color = fontColor)
        }
    }
}

@Composable
private fun BottomBarItem(
    text: String,
    amount: String = "0.0"
) {
    Card(
        modifier = Modifier.padding(vertical = dimens.space2),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(dimens.elevation2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space12),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text)
            Text(text = amount)
        }
    }
}

