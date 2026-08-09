package com.hazrat.auth.ui.support

import androidx.compose.runtime.Immutable
import com.hazrat.domain.repository.NativeSupportPackage
import com.hazrat.model.profile.SupporterStatusModel
import com.hazrat.model.profile.SupporterTickerModel

enum class SupportTab {
    ONE_TIME,
    MONTHLY
}

enum class TipTier(
    val defaultPriceText: String,
    val titleRes: Int,
    val descRes: Int,
    val badgeRes: Int? = null,
    val iconRes: Int,
    val productId: String
) {
    SMALL("$0.99", com.hazrat.ui.R.string.support_tip_small_title, com.hazrat.ui.R.string.support_tip_small_desc, com.hazrat.ui.R.string.support_badge_most_popular, com.hazrat.ui.R.drawable.tasbih, "tip_small"),
    GENEROUS("$2.99", com.hazrat.ui.R.string.support_tip_generous_title, com.hazrat.ui.R.string.support_tip_generous_desc, null, com.hazrat.ui.R.drawable.heart, "tip_generous"),
    SUPPORTER("$4.99", com.hazrat.ui.R.string.support_tip_supporter_title, com.hazrat.ui.R.string.support_tip_supporter_desc, com.hazrat.ui.R.string.support_badge_best_value, com.hazrat.ui.R.drawable.allah, "tip_supporter"),
    PATRON("$9.99", com.hazrat.ui.R.string.support_tip_patron_title, com.hazrat.ui.R.string.support_tip_patron_desc, null, com.hazrat.ui.R.drawable.zakat, "tip_patron"),
    CHAMPION("$19.99", com.hazrat.ui.R.string.support_tip_champion_title, com.hazrat.ui.R.string.support_tip_champion_desc, com.hazrat.ui.R.string.support_badge_top_supporter, com.hazrat.ui.R.drawable.star, "tip_champion")
}

enum class SubscriptionTier(
    val defaultPriceText: String,
    val titleRes: Int,
    val descRes: Int,
    val badgeRes: Int? = null,
    val iconRes: Int,
    val productId: String
) {
    SUPPORTER("$2.99", com.hazrat.ui.R.string.support_sub_supporter_title, com.hazrat.ui.R.string.support_sub_supporter_desc, com.hazrat.ui.R.string.support_badge_most_popular, com.hazrat.ui.R.drawable.heart, "subscribe_small"),
    GUARDIAN("$4.99", com.hazrat.ui.R.string.support_sub_guardian_title, com.hazrat.ui.R.string.support_sub_guardian_desc, com.hazrat.ui.R.string.support_badge_best_value, com.hazrat.ui.R.drawable.quran, "monthly_guardian"),
    PATRON("$9.99", com.hazrat.ui.R.string.support_sub_patron_title, com.hazrat.ui.R.string.support_sub_patron_desc, null, com.hazrat.ui.R.drawable.zakat, "monthly_patron")
}

data class FaqItem(
    val questionRes: Int,
    val answerRes: Int
)

@Immutable
data class SupportUiState(
    val selectedTab: SupportTab = SupportTab.ONE_TIME,
    val selectedTipTier: TipTier? = TipTier.SMALL,
    val selectedSubscriptionTier: SubscriptionTier? = SubscriptionTier.SUPPORTER,
    val nativePackages: List<NativeSupportPackage> = emptyList(),
    val totalSupportedUSD: Double = 0.0,
    val formattedSupportedTotal: String = "$0.00",
    val isOffline: Boolean = false,
    val isPurchasing: Boolean = false,
    val expandedFaqIndex: Int? = null,
    val userSupportModel: SupporterStatusModel? = null,
    val showSubscriptionSuccessDialog: Boolean = false,
    val showTipSuccessDialog: Boolean = false,
    val liveTicket: SupporterTickerModel? = null,
    val recentTickers: List<SupporterTickerModel> = emptyList()
)
