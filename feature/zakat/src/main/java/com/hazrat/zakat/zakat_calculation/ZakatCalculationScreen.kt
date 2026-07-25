package com.hazrat.zakat.zakat_calculation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.TopAppBarTitle
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.getDeviceCurrencySymbol
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatCalculationScreen(
    modifier: Modifier = Modifier,
    uiState: ZakatCalculationState,
    onEvent: (ZakatCalculationEvent) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit = {}
) {
    val currencySymbol = getDeviceCurrencySymbol()
    var showDatePickerDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        TopAppBarTitle(title = "Zakat Calculator")
                        Text(
                            text = "2.5% of qualifying wealth",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    BackIcon(onBackClick = onBackClick)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = dimens.space20, vertical = dimens.space12)
            ) {
                Button(
                    onClick = {
                        if (uiState.isSilverPriceValid) {
                            onEvent(ZakatCalculationEvent.SaveZakatRecord)
                            onSaveClick()
                        }
                    },
                    enabled = uiState.isSilverPriceValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.compButton),
                    shape = RoundedCornerShape(dimens.cornerLg),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = "Save",
                            tint = if (uiState.isSilverPriceValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                        Spacer(modifier = Modifier.width(dimens.space8))
                        Text(
                            text = "Save Zakat Calculation",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (uiState.isSilverPriceValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimens.space20)
        ) {
            // 1. Hero Zakat Payable Header Card
            item {
                Spacer(modifier = Modifier.height(dimens.space12))
                HeroZakatCard(
                    uiState = uiState,
                    currencySymbol = currencySymbol
                )
                Spacer(modifier = Modifier.height(dimens.space16))
            }

            // 2. PROMINENT Silver Price Settings Card
            item {
                SilverPriceSettingsCard(
                    silverPrice = uiState.silverPricePerGram,
                    currencySymbol = currencySymbol,
                    isValid = uiState.isSilverPriceValid,
                    onPriceChange = { onEvent(ZakatCalculationEvent.UpdateSilverPrice(it)) }
                )
                Spacer(modifier = Modifier.height(dimens.space20))
            }

            // 3. Segmented Tab Switcher (Assets vs Liabilities)
            item {
                SegmentedTabSwitcher(
                    activeTab = uiState.activeTab,
                    totalAssetsFormatted = formatAmount(uiState.totalAssets, currencySymbol),
                    totalLiabilitiesFormatted = formatAmount(uiState.totalLiabilities, currencySymbol),
                    isEnabled = uiState.isSilverPriceValid,
                    onTabSelected = { onEvent(ZakatCalculationEvent.SelectTab(it)) }
                )
                Spacer(modifier = Modifier.height(dimens.space16))
            }

            // 4. Tab Content Items
            if (uiState.activeTab == ZakatTab.ASSETS) {
                item {
                    AssetInputCard(
                        title = "Cash & Bank",
                        subtitle = "Cash, savings, checking",
                        iconRes = R.drawable.zakat,
                        value = uiState.cashAndBank,
                        currencySymbol = currencySymbol,
                        isEnabled = uiState.isSilverPriceValid,
                        onValueChange = { onEvent(ZakatCalculationEvent.UpdateCashAndBank(it)) }
                    )
                    Spacer(modifier = Modifier.height(dimens.space12))

                    AssetInputCard(
                        title = "Gold & Jewelry",
                        subtitle = "Gold value in grams",
                        iconRes = R.drawable.special,
                        value = uiState.gold,
                        currencySymbol = currencySymbol,
                        isEnabled = uiState.isSilverPriceValid,
                        onValueChange = { onEvent(ZakatCalculationEvent.UpdateGold(it)) }
                    )
                    Spacer(modifier = Modifier.height(dimens.space12))

                    AssetInputCard(
                        title = "Silver",
                        subtitle = "Silver value in grams",
                        iconRes = R.drawable.prayers,
                        value = uiState.silver,
                        currencySymbol = currencySymbol,
                        isEnabled = uiState.isSilverPriceValid,
                        onValueChange = { onEvent(ZakatCalculationEvent.UpdateSilver(it)) }
                    )
                    Spacer(modifier = Modifier.height(dimens.space12))

                    AssetInputCard(
                        title = "Investments",
                        subtitle = "Stocks, bonds, crypto",
                        iconRes = R.drawable.book,
                        value = uiState.investments,
                        currencySymbol = currencySymbol,
                        isEnabled = uiState.isSilverPriceValid,
                        onValueChange = { onEvent(ZakatCalculationEvent.UpdateInvestments(it)) }
                    )
                    Spacer(modifier = Modifier.height(dimens.space12))

                    AssetInputCard(
                        title = "Business Assets",
                        subtitle = "Inventory, receivables",
                        iconRes = R.drawable.menu_01,
                        value = uiState.businessAssets,
                        currencySymbol = currencySymbol,
                        isEnabled = uiState.isSilverPriceValid,
                        onValueChange = { onEvent(ZakatCalculationEvent.UpdateBusinessAssets(it)) }
                    )
                    Spacer(modifier = Modifier.height(dimens.space12))

                    AssetInputCard(
                        title = "Rental Property",
                        subtitle = "Income-generating property",
                        iconRes = R.drawable.naviconhome,
                        value = uiState.rentalProperty,
                        currencySymbol = currencySymbol,
                        isEnabled = uiState.isSilverPriceValid,
                        onValueChange = { onEvent(ZakatCalculationEvent.UpdateRentalProperty(it)) }
                    )
                    Spacer(modifier = Modifier.height(dimens.space16))
                }
            } else {
                item {
                    AssetInputCard(
                        title = "Debts & Loans",
                        subtitle = "Money you owe others",
                        iconRes = R.drawable.zakat,
                        value = uiState.debtsAndLoans,
                        currencySymbol = currencySymbol,
                        isEnabled = uiState.isSilverPriceValid,
                        onValueChange = { onEvent(ZakatCalculationEvent.UpdateDebtsAndLoans(it)) }
                    )
                    Spacer(modifier = Modifier.height(dimens.space12))

                    AssetInputCard(
                        title = "Due Expenses",
                        subtitle = "Bills, taxes, wages due",
                        iconRes = R.drawable.policy,
                        value = uiState.dueExpenses,
                        currencySymbol = currencySymbol,
                        isEnabled = uiState.isSilverPriceValid,
                        onValueChange = { onEvent(ZakatCalculationEvent.UpdateDueExpenses(it)) }
                    )
                    Spacer(modifier = Modifier.height(dimens.space16))
                }
            }

            // 5. Optional Hawl / Due Date Card with Material 3 DatePicker
            item {
                DueDateReminderCard(
                    dueDate = uiState.dueDate,
                    isReminderEnabled = uiState.isReminderEnabled,
                    isEnabled = uiState.isSilverPriceValid,
                    onOpenDatePicker = { showDatePickerDialog = true },
                    onReminderToggle = { onEvent(ZakatCalculationEvent.ToggleReminder(it)) }
                )
                Spacer(modifier = Modifier.height(dimens.space16))
            }

            // 6. Nisab Reference Card
            item {
                NisabReferenceCard(
                    silverPrice = uiState.silverPricePerGram,
                    nisabThreshold = uiState.silverNisabThreshold,
                    currencySymbol = currencySymbol
                )
                Spacer(modifier = Modifier.height(dimens.space32))
            }
        }
    }

    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            val formatted = sdf.format(Date(millis))
                            onEvent(ZakatCalculationEvent.UpdateDueDate(formatted))
                        }
                        showDatePickerDialog = false
                    }
                ) {
                    Text(
                        text = "OK",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun HeroZakatCard(
    uiState: ZakatCalculationState,
    currencySymbol: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(dimens.elevation2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Zakat Payable",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(dimens.space4))
                    Text(
                        text = "$currencySymbol${formatNumber(uiState.zakatPayable)}",
                        style = MaterialTheme.typography.displayMedium.copy(
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
                        contentDescription = "Coins",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconLg)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(dimens.cornerLg))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .padding(dimens.space12)
                ) {
                    Column {
                        Text(
                            text = "Net Assets",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(dimens.space4))
                        Text(
                            text = "$currencySymbol${formatNumber(uiState.netAssets)}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(dimens.cornerLg))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .padding(dimens.space12)
                ) {
                    Column {
                        Text(
                            text = "Nisab Status",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(dimens.space4))
                        Text(
                            text = if (!uiState.isSilverPriceValid) "Enter Silver Price"
                            else if (uiState.isEligible) "Eligible"
                            else "Not Eligible",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (!uiState.isSilverPriceValid) MaterialTheme.colorScheme.tertiary
                            else if (uiState.isEligible) customColors.accentColor
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
                    .padding(horizontal = dimens.space12, vertical = dimens.space8)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ⓘ ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = if (uiState.isSilverPriceValid)
                            "Nisab: Silver 612.36g @ $currencySymbol${uiState.silverPricePerGram}/g"
                        else
                            "Please enter today's silver price per gram below to enable calculation",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun SilverPriceSettingsCard(
    silverPrice: String,
    currencySymbol: String,
    isValid: Boolean,
    onPriceChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(dimens.space40)
                        .clip(RoundedCornerShape(dimens.cornerMd))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.prayers),
                        contentDescription = "Silver",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconMd)
                    )
                }

                Spacer(modifier = Modifier.width(dimens.space12))

                Column {
                    Text(
                        text = "Silver Price Settings (Required)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Price per gram in your local currency",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimens.cornerLg))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = dimens.space16, vertical = dimens.space12)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$currencySymbol ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    BasicTextField(
                        value = silverPrice,
                        onValueChange = onPriceChange,
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (silverPrice.isEmpty()) {
                                Text(
                                    text = "Enter Today's Silver Price",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (!isValid) {
                Spacer(modifier = Modifier.height(dimens.space8))
                Text(
                    text = "⚠️ Enter silver price to unlock Zakat asset input fields",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun SegmentedTabSwitcher(
    activeTab: ZakatTab,
    totalAssetsFormatted: String,
    totalLiabilitiesFormatted: String,
    isEnabled: Boolean,
    onTabSelected: (ZakatTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isEnabled) 1.0f else 0.5f)
            .clip(RoundedCornerShape(dimens.cornerLg))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(dimens.space4)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimens.space4)
        ) {
            val isAssetsActive = activeTab == ZakatTab.ASSETS

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(if (isAssetsActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(enabled = isEnabled) { onTabSelected(ZakatTab.ASSETS) }
                    .padding(vertical = dimens.space12),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Assets ($totalAssetsFormatted)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isAssetsActive) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = if (isAssetsActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(if (!isAssetsActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(enabled = isEnabled) { onTabSelected(ZakatTab.LIABILITIES) }
                    .padding(vertical = dimens.space12),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Liabilities ($totalLiabilitiesFormatted)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (!isAssetsActive) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = if (!isAssetsActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AssetInputCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    value: String,
    currencySymbol: String,
    isEnabled: Boolean,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isEnabled) 1.0f else 0.5f),
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.space40)
                        .clip(RoundedCornerShape(dimens.cornerMd))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconMd)
                    )
                }

                Spacer(modifier = Modifier.width(dimens.space12))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimens.cornerLg))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = dimens.space16, vertical = dimens.space12),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$currencySymbol ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        enabled = isEnabled,
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (value.isEmpty()) {
                                Text(
                                    text = "0",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun DueDateReminderCard(
    dueDate: String,
    isReminderEnabled: Boolean,
    isEnabled: Boolean,
    onOpenDatePicker: () -> Unit,
    onReminderToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isEnabled) 1.0f else 0.5f),
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.space40)
                        .clip(RoundedCornerShape(dimens.cornerMd))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Due Date",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconMd)
                    )
                }

                Spacer(modifier = Modifier.width(dimens.space12))

                Column {
                    Text(
                        text = "Zakat Hawl / Due Date (Optional)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Track your 1 lunar year (Hawl) completion date",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimens.cornerLg))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(enabled = isEnabled) { onOpenDatePicker() }
                    .padding(horizontal = dimens.space16, vertical = dimens.space12)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (dueDate.isNotBlank()) dueDate else "Select Zakat Due Date",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (dueDate.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Select Date",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Enable Annual Reminder Notification",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Get notified 3 days before your Zakat is due",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Switch(
                    checked = isReminderEnabled,
                    onCheckedChange = onReminderToggle,
                    enabled = isEnabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}

@Composable
private fun NisabReferenceCard(
    silverPrice: String,
    nisabThreshold: Double,
    currencySymbol: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Text(
                text = "Nisab Reference",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            painter = painterResource(id = R.drawable.prayers),
                            contentDescription = "Silver Nisab",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(dimens.iconMd)
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.space12))

                    Column {
                        Text(
                            text = "Silver Nisab",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "612.36 grams @ $currencySymbol$silverPrice/g",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Text(
                    text = "$currencySymbol${formatNumber(nisabThreshold)}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatAmount(amount: Double, symbol: String): String {
    return "$symbol${formatNumber(amount)}"
}

private fun formatNumber(amount: Double): String {
    return try {
        val format = NumberFormat.getNumberInstance(Locale.US)
        format.maximumFractionDigits = 0
        format.format(amount.toInt())
    } catch (_: Exception) {
        amount.toInt().toString()
    }
}
