package com.hazrat.domain.repository

import android.app.Activity
import kotlinx.coroutines.flow.Flow

/**
 * Domain model representing customer support info.
 */
data class CustomerSupportInfo(
    val totalSupportedUSD: Double = 0.0,
    val isMonthlySupporter: Boolean = false,
    val activeSubscriptionId: String? = null
)

/**
 * Clean domain wrapper for localized native package from Google Play / RevenueCat.
 */
data class NativeSupportPackage(
    val packageId: String,
    val productId: String,
    val formattedPrice: String,
    val rawPriceMicros: Long,
    val currencyCode: String
)

/**
 * Clean domain interface for Support Islam 24 billing engine.
 */
interface BillingRepository {

    /**
     * Observes real-time customer support stats (total supported amount and active subscription).
     */
    fun observeCustomerSupportInfo(): Flow<CustomerSupportInfo>

    /**
     * Fetches current offerings & native localized packages from RevenueCat / Google Play.
     */
    suspend fun fetchNativePackages(): Result<List<NativeSupportPackage>>

    /**
     * Purchases a native package (one-time tip or monthly subscription).
     */
    suspend fun purchasePackage(activity: Activity, packageToPurchase: NativeSupportPackage): Result<CustomerSupportInfo>

    /**
     * Restores purchases across uninstalls or device switches via Google Play receipts.
     */
    suspend fun restorePurchases(): Result<CustomerSupportInfo>
}
