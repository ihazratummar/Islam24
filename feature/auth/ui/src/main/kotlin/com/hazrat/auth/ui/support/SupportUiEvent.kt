package com.hazrat.auth.ui.support

import android.app.Activity

sealed interface SupportUiEvent {
    data class SelectTab(val tab: SupportTab) : SupportUiEvent
    data class SelectTipTier(val tier: TipTier) : SupportUiEvent
    data class SelectSubscriptionTier(val tier: SubscriptionTier) : SupportUiEvent
    data class ToggleFaq(val index: Int) : SupportUiEvent
    data class PurchaseCurrentSelection(val activity: Activity) : SupportUiEvent
    data object RestorePurchases : SupportUiEvent
    data object DismissSuccessDialog : SupportUiEvent
    data object Refresh : SupportUiEvent
}


sealed interface SupportEffect {
    data class Error(val message: String) : SupportEffect
    data class Success(val message: String) : SupportEffect
    data object NavigateBack: SupportEffect
}