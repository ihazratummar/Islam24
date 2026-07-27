package com.hazrat.auth.ui.support

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import java.text.NumberFormat
import java.util.Locale

/**
 * Support Islam 24 Screen — Pure Voluntary Sadaqah & Tipping Engine.
 */
@Composable
fun SupportIslam24Screen(
    viewModel: SupportViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val faqList = listOf(
        FaqItem(R.string.support_faq_q1, R.string.support_faq_a1),
        FaqItem(R.string.support_faq_q2, R.string.support_faq_a2),
        FaqItem(R.string.support_faq_q3, R.string.support_faq_a3),
        FaqItem(R.string.support_faq_q4, R.string.support_faq_a4),
        FaqItem(R.string.support_faq_q5, R.string.support_faq_a5),
        FaqItem(R.string.support_faq_q6, R.string.support_faq_a6)
    )

    Scaffold(
        topBar = {
            BasicTopBar(
                topBarTitle = stringResource(R.string.support_title),
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = dimens.space16, vertical = dimens.space12),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // Offline Connection Warning Banner
            if (uiState.isOffline) {
                item {
                    OfflineWarningBanner()
                }
            }

            // Header Emblem & Inspirational Tagline
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimens.iconXl * 1.8f)
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
                        text = stringResource(R.string.support_every_feature_free),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(dimens.space32)
                                .height(dimens.divider)
                                .background(customColors.secondaryText.copy(alpha = 0.3f))
                        )
                        Text(
                            text = "◆",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Box(
                            modifier = Modifier
                                .width(dimens.space32)
                                .height(dimens.divider)
                                .background(customColors.secondaryText.copy(alpha = 0.3f))
                        )
                    }

                    Text(
                        text = stringResource(R.string.support_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = customColors.secondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = dimens.space16)
                    )

                    Text(
                        text = stringResource(R.string.support_sadaqah_tagline),
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = dimens.space16)
                    )
                }
            }

            // Metrics Summary Container
            item {
                SupportMetricsCard(
                    supportersCount = uiState.supportersCount,
                    formattedSupportedTotal = uiState.formattedSupportedTotal
                )
            }

            // Tab Selector (One-Time Tip vs Monthly Support)
            item {
                SupportTabSelector(
                    selectedTab = uiState.selectedTab,
                    onTabSelected = { viewModel.onEvent(SupportUiEvent.SelectTab(it)) }
                )
            }

            // Tier Options List
            if (uiState.selectedTab == SupportTab.ONE_TIME) {
                itemsIndexed(items = TipTier.entries) { index, tier ->
                    val nativePkg = uiState.nativePackages.find { pkg ->
                        pkg.productId.equals(tier.productId, ignoreCase = true) ||
                                pkg.packageId.equals(tier.name.lowercase(), ignoreCase = true) ||
                                pkg.productId.contains(tier.productId, ignoreCase = true) ||
                                tier.productId.contains(pkg.productId, ignoreCase = true)
                    } ?: uiState.nativePackages.getOrNull(index)

                    val displayPrice = nativePkg?.formattedPrice ?: tier.defaultPriceText

                    TipTierCard(
                        tier = tier,
                        displayPrice = displayPrice,
                        isSelected = uiState.selectedTipTier == tier,
                        onClick = { viewModel.onEvent(SupportUiEvent.SelectTipTier(tier)) }
                    )
                }
            } else {
                itemsIndexed(items = SubscriptionTier.entries) { index, tier ->
                    val nativePkg = uiState.nativePackages.find { pkg ->
                        pkg.productId.equals(tier.productId, ignoreCase = true) ||
                                pkg.packageId.equals(tier.productId, ignoreCase = true) ||
                                pkg.productId.contains(tier.productId, ignoreCase = true) ||
                                tier.productId.contains(pkg.productId, ignoreCase = true)
                    } ?: uiState.nativePackages.getOrNull(index)

                    val displayPrice = nativePkg?.formattedPrice ?: tier.defaultPriceText

                    SubscriptionTierCard(
                        tier = tier,
                        displayPrice = displayPrice,
                        isSelected = uiState.selectedSubscriptionTier == tier,
                        onClick = { viewModel.onEvent(SupportUiEvent.SelectSubscriptionTier(tier)) }
                    )
                }
            }

            // Action CTA Button & Payment Security Disclaimer
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    val isActionable = (uiState.selectedTab == SupportTab.ONE_TIME && uiState.selectedTipTier != null) ||
                            (uiState.selectedTab == SupportTab.MONTHLY && uiState.selectedSubscriptionTier != null)

                    val selectedPriceText = if (uiState.selectedTab == SupportTab.ONE_TIME) {
                        val tier = uiState.selectedTipTier
                        val nativePkg = uiState.nativePackages.find { pkg ->
                            pkg.productId.equals(tier?.productId, ignoreCase = true) ||
                                    pkg.packageId.equals(tier?.name?.lowercase(), ignoreCase = true) ||
                                    pkg.productId.contains(tier?.productId.orEmpty(), ignoreCase = true)
                        } ?: uiState.nativePackages.getOrNull(tier?.ordinal ?: 0)
                        nativePkg?.formattedPrice ?: tier?.defaultPriceText.orEmpty()
                    } else {
                        val tier = uiState.selectedSubscriptionTier
                        val nativePkg = uiState.nativePackages.find { pkg ->
                            pkg.productId.equals(tier?.productId, ignoreCase = true) ||
                                    pkg.packageId.equals(tier?.productId, ignoreCase = true) ||
                                    pkg.productId.contains(tier?.productId.orEmpty(), ignoreCase = true)
                        } ?: uiState.nativePackages.getOrNull(tier?.ordinal ?: 0)
                        nativePkg?.formattedPrice ?: tier?.defaultPriceText.orEmpty()
                    }

                    val buttonText = when {
                        !isActionable -> stringResource(R.string.support_cta_choose)
                        uiState.selectedTab == SupportTab.ONE_TIME -> stringResource(R.string.support_cta_tip, selectedPriceText)
                        else -> stringResource(R.string.support_cta_subscribe, selectedPriceText)
                    }

                    Button(
                        onClick = {
                            if (isActionable && context is android.app.Activity) {
                                viewModel.onEvent(SupportUiEvent.PurchaseCurrentSelection(context))
                            }
                        },
                        enabled = isActionable && !uiState.isPurchasing && !uiState.isOffline,
                        shape = RoundedCornerShape(dimens.cornerLg),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimens.compButton)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                        ) {
                            if (isActionable) {
                                Icon(
                                    painter = painterResource(id = R.drawable.heart),
                                    contentDescription = null,
                                    modifier = Modifier.size(dimens.iconSm),
                                    tint = Color.Unspecified
                                )
                            }
                            Text(
                                text = buttonText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Text(
                        text = stringResource(R.string.support_secure_payment),
                        style = MaterialTheme.typography.labelSmall,
                        color = customColors.secondaryText.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Gift Disclaimer Card (Islam 24 is 100% free)
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
                        Icon(
                            painter = painterResource(id = R.drawable.allah),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(dimens.iconMd)
                                .padding(top = dimens.space2)
                        )

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
                                    onToggle = { viewModel.onEvent(SupportUiEvent.ToggleFaq(index)) },
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
    }
}

/**
 * Appealing Offline Warning Banner.
 */
@Composable
private fun OfflineWarningBanner(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.iconLg)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.alert),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(dimens.iconSm)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = stringResource(R.string.support_offline_title),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = stringResource(R.string.support_offline_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f)
                )
            }
        }
    }
}

/**
 * Metrics summary box showing Supporters count, Total supported amount, and Ad-Free indicator.
 */
@Suppress("NonObservableLocale")
@Composable
private fun SupportMetricsCard(
    supportersCount: Int,
    formattedSupportedTotal: String,
    modifier: Modifier = Modifier
) {
    val locale = androidx.compose.ui.platform.LocalConfiguration.current.locales[0]
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.space16, horizontal = dimens.space8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricColumn(
                value = String.format(locale, "%,d", supportersCount),
                label = stringResource(R.string.support_supporters_count),
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .width(dimens.divider)
                    .height(dimens.space32)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )

            MetricColumn(
                value = formattedSupportedTotal,
                label = stringResource(R.string.support_you_supported),
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .width(dimens.divider)
                    .height(dimens.space32)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )

            MetricColumn(
                value = "100%",
                label = stringResource(R.string.support_ad_free),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricColumn(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.space2)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = customColors.secondaryText
        )
    }
}

/**
 * Tab Selector Pill Row (One-Time Tip vs Monthly Support).
 */
@Composable
private fun SupportTabSelector(
    selectedTab: SupportTab,
    onTabSelected: (SupportTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(dimens.space4),
        horizontalArrangement = Arrangement.spacedBy(dimens.space4)
    ) {
        TabPill(
            text = stringResource(R.string.support_one_time_tip),
            isSelected = selectedTab == SupportTab.ONE_TIME,
            onClick = { onTabSelected(SupportTab.ONE_TIME) },
            modifier = Modifier.weight(1f)
        )
        TabPill(
            text = stringResource(R.string.support_monthly_support),
            isSelected = selectedTab == SupportTab.MONTHLY,
            onClick = { onTabSelected(SupportTab.MONTHLY) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.cornerMd))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(vertical = dimens.space12),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
        )
    }
}

/**
 * One-Time Tip Tier Card.
 */
@Composable
private fun TipTierCard(
    tier: TipTier,
    displayPrice: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .border(
                width = if (isSelected) dimens.divider * 2 else dimens.divider,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(dimens.cornerLg)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            // Icon Square Box
            TierIconBox(iconRes = tier.iconRes)

            // Title, Badge, and Description
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    Text(
                        text = stringResource(tier.titleRes),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    tier.badgeRes?.let { badgeRes ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimens.cornerFull))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                                .padding(horizontal = dimens.space8, vertical = dimens.space2)
                        ) {
                            Text(
                                text = stringResource(badgeRes),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(tier.descRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = customColors.secondaryText
                )
            }

            // Price Column
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = displayPrice,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "one-time",
                    style = MaterialTheme.typography.labelSmall,
                    color = customColors.secondaryText.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Monthly Subscription Tier Card.
 */
@Composable
private fun SubscriptionTierCard(
    tier: SubscriptionTier,
    displayPrice: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .border(
                width = if (isSelected) dimens.divider * 2 else dimens.divider,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(dimens.cornerLg)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                // Icon Square Box
                TierIconBox(iconRes = tier.iconRes)

                // Title, Badge, and Description
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Text(
                            text = stringResource(tier.titleRes),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        tier.badgeRes?.let { badgeRes ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(dimens.cornerFull))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                                    .padding(horizontal = dimens.space8, vertical = dimens.space2)
                            ) {
                                Text(
                                    text = stringResource(badgeRes),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    Text(
                        text = stringResource(tier.descRes),
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText
                    )
                }

                // Price Column
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Text(
                        text = displayPrice,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "/ month",
                        style = MaterialTheme.typography.labelSmall,
                        color = customColors.secondaryText.copy(alpha = 0.7f)
                    )
                }
            }

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimens.divider)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    )
                    Spacer(modifier = Modifier.height(dimens.space8))
                    Text(
                        text = stringResource(R.string.support_sub_billing_disclaimer, displayPrice),
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TierIconBox(
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(dimens.iconXl)
            .clip(RoundedCornerShape(dimens.cornerMd))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(dimens.iconMd)
        )
    }
}

@Composable
private fun FaqAccordionRow(
    faq: FaqItem,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    showDivider: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(faq.questionRes),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(
                    id = if (isExpanded) R.drawable.arrowup else R.drawable.down_arrow
                ),
                contentDescription = null,
                tint = customColors.secondaryText,
                modifier = Modifier.size(dimens.iconSm)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = stringResource(faq.answerRes),
                style = MaterialTheme.typography.bodySmall,
                color = customColors.secondaryText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimens.space16, end = dimens.space16, bottom = dimens.space16)
            )
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.divider)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            )
        }
    }
}
