package com.hazrat.islam24.core.presentation.zakat.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.zakat.ZakatEvent
import com.hazrat.islam24.core.presentation.zakat.ZakatState

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
    moneyInfoNav: () -> Unit = {},
    goldInfoNav: () -> Unit = {},
    silverInfoNav: () -> Unit = {},
    monthInfoNav: () -> Unit = {},
    totalDebtInfoNav: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.zakat_calculator)) },
                actions = {
                    TextButton(
                        onClick = {
                            zakatEvent(ZakatEvent.SaveZakat)
                            onSaveClick()
                            zakatEvent(ZakatEvent.ResetAllState)
                            Toast.makeText(context,
                                context.getString(R.string.done), Toast.LENGTH_SHORT).show()
                        },
                        enabled = zakatState.money != "0.0" || zakatState.gold != "0.0" ||
                                zakatState.silver != "0.0" || zakatState.tradeAmount != "0.0"
                                || zakatState.monthCost != "0.0" || zakatState.debt != "0.0"
                    ) {
                        Text(text = "Save")
                    }
                    IconButton(
                        onClick = {
                            zakatEvent(ZakatEvent.ResetAllState)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClick()
                            zakatEvent(ZakatEvent.ResetAllState)
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.backicon), contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .align(Alignment.TopCenter)
            ) {
                item {
                    CalculateItems(
                        text = stringResource(R.string.money),
                        amount = zakatState.money,
                        onClick = { zakatEvent(ZakatEvent.OpenMoneyDialog) },
                        infoNavigation = {
                            moneyInfoNav()
                        }
                    )
                    CalculateItems(
                        text = stringResource(R.string.gold),
                        amount = zakatState.gold,
                        onClick = { zakatEvent(ZakatEvent.OpenGoldDialog) },
                        infoNavigation = { goldInfoNav() }
                    )
                    CalculateItems(
                        text = stringResource(R.string.silver),
                        amount = zakatState.silver,
                        onClick = { zakatEvent(ZakatEvent.OpenSilverDialog) },
                        infoNavigation = { silverInfoNav() }
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
                        infoNavigation = { monthInfoNav() },
                        fontColor = MaterialTheme.colorScheme.error
                    )
                    CalculateItems(
                        text = stringResource(R.string.total_debt_on_you),
                        amount = zakatState.debt,
                        onClick = { zakatEvent(ZakatEvent.OpenDebtDialog) },
                        infoNavigation = { totalDebtInfoNav() },
                        fontColor = MaterialTheme.colorScheme.error
                    )
                }
            }

            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.BottomCenter),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
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
            }
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
            }
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
            }
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
            }
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
            }
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
    onDone: () -> Unit = {}
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
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Enter Amount")
            Spacer(modifier = Modifier.height(15.dp))
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
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onClick()
                }
            ) {
                Text(text = "Add")
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
            .padding(vertical = 5.dp)
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
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontWeight = FontWeight.SemiBold)
            IconButton(onClick = { infoNavigation() }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Information")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = amount, fontWeight = FontWeight.SemiBold, color = fontColor)
        }
    }
}

@Composable
private fun BottomBarItem(
    text: String,
    onClick: () -> Unit = {},
    amount: String = "0.0"
) {
    Card(
        modifier = Modifier.padding(vertical = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text)
            Text(text = amount)
        }
    }
}

