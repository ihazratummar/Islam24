package com.hazrat.auth.ui.support

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.TextButton
import com.hazrat.auth.ui.support.components.ActiveSubscriberCard
import com.hazrat.auth.ui.support.components.FaqAccordionRow
import com.hazrat.auth.ui.support.components.OfflineWarningBanner
import com.hazrat.auth.ui.support.components.RecentSupportersSection
import com.hazrat.auth.ui.support.components.SubscriptionTierCard
import com.hazrat.auth.ui.support.components.SupportLiveTickerOverlay
import com.hazrat.auth.ui.support.components.SupportMetricsCard
import com.hazrat.auth.ui.support.components.SupportSuccessDialog
import com.hazrat.auth.ui.support.components.SupportTabSelector
import com.hazrat.auth.ui.support.components.TipTierCard
import com.hazrat.model.profile.SupporterTickerModel
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.common.IslamicLoadingOverlay
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.ui.theme.isUserSubscribed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.ui.platform.LocalLocale
import androidx.core.net.toUri

/**
 * Clean, modular Support Islam 24 screen composable.
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun SupportIslam24Screen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    uiState: SupportUiState,
    onEvent: (SupportUiEvent) -> Unit,
    supportEffect: SharedFlow<SupportEffect>? = null
) {
    val context = LocalContext.current
    val snackbarState = remember { SnackbarHostState() }
    val isSubscribed = isUserSubscribed

    var visibleTicker by remember { mutableStateOf<SupporterTickerModel?>(null) }
    val liveTicket = uiState.liveTicket

    LaunchedEffect(liveTicket) {
        if (liveTicket != null) {
            visibleTicker = liveTicket
            delay(4000.milliseconds)
            visibleTicker = null
        }
    }

    LaunchedEffect(isSubscribed) {
        if (isSubscribed) {
            onEvent(SupportUiEvent.SelectTab(SupportTab.ONE_TIME))
        }
    }

    LaunchedEffect(Unit) {
        supportEffect?.collect { effect ->
            when (effect) {
                is SupportEffect.Error -> {
                    snackbarState.showSnackbar(effect.message)
                }

                is SupportEffect.Success -> {
                    snackbarState.showSnackbar(effect.message)
                }

                is SupportEffect.NavigateBack -> {
                    onBackClick()
                }
            }
        }
    }

    val faqList = listOf(
        FaqItem(R.string.support_faq_q1, R.string.support_faq_a1),
        FaqItem(R.string.support_faq_q2, R.string.support_faq_a2),
        FaqItem(R.string.support_faq_q3, R.string.support_faq_a3),
        FaqItem(R.string.support_faq_q4, R.string.support_faq_a4),
        FaqItem(R.string.support_faq_q5, R.string.support_faq_a5),
        FaqItem(R.string.support_faq_q6, R.string.support_faq_a6)
    )

    val headerTitle = if (isSubscribed) "You're Part of the Family" else stringResource(R.string.support_every_feature_free)
    val headerSubtitle = if (isSubscribed) "Your monthly gift keeps Islam 24 alive for the entire Ummah. Every prayer, every dhikr — you are part of it." else stringResource(R.string.support_subtitle)
    val headerTagline = if (isSubscribed) "May Allah multiply your reward in both worlds." else stringResource(R.string.support_sadaqah_tagline)

    val activeSubPackage = uiState.nativePackages.find { pkg ->
        SubscriptionTier.entries.any { tier ->
            pkg.productId.contains(tier.productId, ignoreCase = true) ||
                    pkg.packageId.contains(tier.productId, ignoreCase = true)
        }
    }
    val activeSubscriberDisplayPrice = activeSubPackage?.formattedPrice?.let { "$it/month" } ?: "$9.99/month"

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        topBar = {
            BasicTopBar(
                topBarTitle = stringResource(R.string.support_title),
                onBackClick = onBackClick,
                actions = {
                    TextButton(
                        onClick = { onEvent(SupportUiEvent.RestorePurchases) }
                    ) {
                        Text(
                            text = stringResource(R.string.support_restore),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        IslamicLoadingOverlay(
            isLoading = uiState.isPurchasing,
            loadingText = "Processing Support..."
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = dimens.space16, vertical = dimens.space12),
                        verticalArrangement = Arrangement.spacedBy(dimens.space16)
                    ) {
                        // Active Subscriber Banner
                        if (isSubscribed) {
                            item {
                                ActiveSubscriberCard(
                                    displayPrice = activeSubscriberDisplayPrice,
                                    memberSinceDate = SimpleDateFormat("MMMM d, yyyy", LocalLocale.current.platformLocale).format(Date()),
                                    onCancelClick = {
                                        val playStoreUrl = "https://play.google.com/store/account/subscriptions?package=${context.packageName}"
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW,
                                                playStoreUrl.toUri()).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            val fallbackUrl = "https://play.google.com/store/account/subscriptions"
                                            val intent = Intent(Intent.ACTION_VIEW,
                                                fallbackUrl.toUri()).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(intent)
                                        }
                                    }
                                )
                            }
                        }

                        // Offline Connection Warning Banner
                        if (uiState.isOffline) {
                            item {
                                OfflineWarningBanner()
                            }
                        }

                        // Header Section: Title & Subtitle Card
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(dimens.space8)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(dimens.iconXl * 1.5f)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.heart),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(dimens.iconLg)
                                    )
                                }

                                Text(
                                    text = headerTitle,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = headerSubtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = customColors.secondaryText,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = dimens.space8)
                                )

                                Text(
                                    text = headerTagline,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = dimens.space4)
                                )
                            }
                        }

                        // Metrics Box: Supporters Count, Total Supported, Ad-Free
                        item {
                            SupportMetricsCard(
                                supportersCount = uiState.userSupportModel?.totalSupporter ?: 0,
                                formattedSupportedTotal = "${uiState.userSupportModel?.totalContributionLocal ?: uiState.formattedSupportedTotal}"
                            )
                        }

                        // Tab Selector Pill: One-Time Tip vs Monthly Support (Hidden for Active Subscribers)
                        item {
                            SupportTabSelector(
                                selectedTab = uiState.selectedTab,
                                onTabSelected = { onEvent(SupportUiEvent.SelectTab(it)) },
                                isSupporter = isSubscribed
                            )
                        }

                        // Options List (One-Time Tip Tiers OR Monthly Subscription Tiers)
                        if (uiState.selectedTab == SupportTab.ONE_TIME) {
                            itemsIndexed(
                                items = TipTier.entries,
                                key = { _, tier -> tier.name }
                            ) { index, tier ->
                                val matchedPkg = uiState.nativePackages.find { pkg ->
                                    pkg.productId.contains(tier.productId, ignoreCase = true) ||
                                            pkg.packageId.contains(tier.productId, ignoreCase = true) ||
                                            tier.productId.contains(pkg.productId, ignoreCase = true)
                                } ?: uiState.nativePackages.getOrNull(index)
                                val formattedPrice = matchedPkg?.formattedPrice ?: tier.defaultPriceText

                                TipTierCard(
                                    tier = tier,
                                    displayPrice = formattedPrice,
                                    isSelected = uiState.selectedTipTier == tier,
                                    onClick = { onEvent(SupportUiEvent.SelectTipTier(tier)) }
                                )
                            }
                        } else {
                            itemsIndexed(
                                items = SubscriptionTier.entries,
                                key = { _, tier -> tier.name }
                            ) { index, tier ->
                                val matchedPkg = uiState.nativePackages.find { pkg ->
                                    pkg.productId.contains(tier.productId, ignoreCase = true) ||
                                            pkg.packageId.contains(tier.productId, ignoreCase = true) ||
                                            tier.productId.contains(pkg.productId, ignoreCase = true)
                                } ?: uiState.nativePackages.getOrNull(index)
                                val formattedPrice = matchedPkg?.formattedPrice ?: tier.defaultPriceText

                                SubscriptionTierCard(
                                    tier = tier,
                                    displayPrice = formattedPrice,
                                    isSelected = uiState.selectedSubscriptionTier == tier,
                                    onClick = { onEvent(SupportUiEvent.SelectSubscriptionTier(tier)) }
                                )
                            }
                        }

                        // Primary Action Button (Support / Tip Now)
                        if (!isSubscribed) {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(dimens.space8)
                                ) {
                                    val buttonText = if (uiState.selectedTab == SupportTab.ONE_TIME) {
                                        val tierTitle = uiState.selectedTipTier?.let { stringResource(it.titleRes) } ?: "Tip"
                                        val price = uiState.selectedTipTier?.let { tier ->
                                            val index = TipTier.entries.indexOf(tier)
                                            val matchedPkg = uiState.nativePackages.find { pkg ->
                                                pkg.productId.contains(tier.productId, ignoreCase = true) ||
                                                        pkg.packageId.contains(tier.productId, ignoreCase = true) ||
                                                        tier.productId.contains(pkg.productId, ignoreCase = true)
                                            } ?: uiState.nativePackages.getOrNull(index)
                                            matchedPkg?.formattedPrice ?: tier.defaultPriceText
                                        } ?: "$0"
                                        "Send $tierTitle · $price"
                                    } else {
                                        val price = uiState.selectedSubscriptionTier?.let { tier ->
                                            val index = SubscriptionTier.entries.indexOf(tier)
                                            val matchedPkg = uiState.nativePackages.find { pkg ->
                                                pkg.productId.contains(tier.productId, ignoreCase = true) ||
                                                        pkg.packageId.contains(tier.productId, ignoreCase = true) ||
                                                        tier.productId.contains(pkg.productId, ignoreCase = true)
                                            } ?: uiState.nativePackages.getOrNull(index)
                                            matchedPkg?.formattedPrice ?: tier.defaultPriceText
                                        } ?: "$0"
                                        "Become Monthly Patron · $price/mo"
                                    }

                                    val isActionable = if (uiState.selectedTab == SupportTab.ONE_TIME) {
                                        uiState.selectedTipTier != null
                                    } else {
                                        uiState.selectedSubscriptionTier != null
                                    }

                                    Button(
                                        onClick = {
                                            val activity = context as? android.app.Activity
                                            if (activity != null) {
                                                onEvent(SupportUiEvent.PurchaseCurrentSelection(activity))
                                            } else {
                                                Toast.makeText(context, "Unable to launch purchase flow", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        enabled = isActionable && !uiState.isPurchasing,
                                        shape = RoundedCornerShape(dimens.cornerLg),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(dimens.compButton)
                                    ) {
                                        Text(
                                            text = buttonText,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = Color.White
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.password),
                                            contentDescription = null,
                                            tint = customColors.secondaryText,
                                            modifier = Modifier.size(dimens.iconXs)
                                        )
                                        Spacer(modifier = Modifier.size(dimens.space4))
                                        Text(
                                            text = stringResource(R.string.support_secure_payment),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = customColors.secondaryText
                                        )
                                    }
                                }
                            }
                        }

                        // Independent Developer Note Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(dimens.cornerLg),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(dimens.space16),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(dimens.iconLg)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.profile),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(dimens.iconSm)
                                        )
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(dimens.space4)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.support_disclaimer_title),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Text(
                                            text = stringResource(R.string.support_disclaimer_desc),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = customColors.secondaryText
                                        )
                                    }
                                }
                            }
                        }

                        // Recent Community Supporters (Top 10 cached tickers)
                        if (uiState.recentTickers.isNotEmpty()) {
                            item {
                                RecentSupportersSection(recentTickers = uiState.recentTickers)
                            }
                        }

                        // Common Questions FAQ Accordion
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(dimens.space12)
                            ) {
                                Text(
                                    text = stringResource(R.string.support_faq_title),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(top = dimens.space8)
                                )

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(dimens.cornerLg),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    )
                                ) {
                                    Column {
                                        faqList.forEachIndexed { index, faq ->
                                            FaqAccordionRow(
                                                faq = faq,
                                                isExpanded = uiState.expandedFaqIndex == index,
                                                onToggle = { onEvent(SupportUiEvent.ToggleFaq(index)) },
                                                showDivider = index < faqList.lastIndex
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(dimens.space32))
                        }
                    }

                    // Real-Time Supporter Live Ticker Overlay at bottom
                    SupportLiveTickerOverlay(
                        visibleTicker = visibleTicker,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }

            // Custom Success Dialog Overlays
            if (uiState.showSubscriptionSuccessDialog) {
                SupportSuccessDialog(
                    title = stringResource(R.string.support_dialog_title),
                    description = stringResource(R.string.support_subscription_success_desc),
                    subtext = stringResource(R.string.support_may_allah_accept),
                    onDismiss = { onEvent(SupportUiEvent.DismissSuccessDialog) }
                )
            }

            if (uiState.showTipSuccessDialog) {
                SupportSuccessDialog(
                    title = stringResource(R.string.support_dialog_title),
                    description = stringResource(R.string.support_tip_success_desc),
                    subtext = stringResource(R.string.support_may_allah_accept),
                    onDismiss = { onEvent(SupportUiEvent.DismissSuccessDialog) }
                )
            }
        }
    }
}
