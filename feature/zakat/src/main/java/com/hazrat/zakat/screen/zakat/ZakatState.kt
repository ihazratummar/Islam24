package com.hazrat.zakat.screen.zakat

data class ZakatState(
    val silverPrice: String = "",
    val nisabAmount: String = "0.0",
    val money: String = "0.0",
    val bankCash: String = "0.0",
    val gold: String = "0.0",
    val silver: String = "0.0",
    val tradeAmount: String = "0.0",
    val monthCost: String = "0.0",
    val debt: String = "0.0",

    val totalAsset: Double = 0.0,
    val zakatAmount: Double = 0.0,

    val isMoneyDialogOpen: Boolean = false,
    val isBankCashDialogOpen: Boolean = false,
    val isGoldDialogOpen: Boolean = false,
    val isSilverDialogOpen: Boolean = false,
    val isTradeAmountDialogOpen: Boolean = false,
    val isMonthCostDialogOpen: Boolean = false,
    val isDebtDialogOpen: Boolean = false,

    val isSilverPriceValid: Boolean = false,
    val isHandCashPriceValid: Boolean = false,
    val isBankCashPriceValid: Boolean = false,
    val isGoldPriceValid: Boolean = false,
    val isSilverValid: Boolean = false,
    val isTradeAmountValid: Boolean = false,
    val isDebtAmountValid: Boolean = false,
    val isMonthCostValid: Boolean = false,


    val isGoldInfoDialogOpen: Boolean = false,
    val isSilverInfoDialogOpen: Boolean = false,
    val isMoneyInfoDialogOpen: Boolean = false,
    val isBankCashInfoDialogOpen: Boolean = false,
    val isTradeAmountInfoDialogOpen: Boolean = false,
    val isMonthCostInfoDialogOpen: Boolean = false,
    val isDebtInfoDialogOpen: Boolean = false,

)

