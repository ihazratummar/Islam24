package com.hazrat.auth.data.billing

import android.app.Activity
import android.util.Log
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.BillingRepository
import com.hazrat.domain.repository.CustomerSupportInfo
import com.hazrat.domain.repository.NativeSupportPackage
import com.revenuecat.purchases.Package
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class BillingRepositoryImpl(
    private val revenueCatBillingDataSource: RevenueCatBillingDataSource,
    private val userDataStore: UserDataStore
) : BillingRepository {

    companion object {
        private const val TAG = "BillingRepositoryImpl"
    }

    // Cache of real RevenueCat Package objects keyed by package identifier and product ID
    private val cachedRcPackages = mutableMapOf<String, Package>()

    override fun observeCustomerSupportInfo(): Flow<CustomerSupportInfo> {
        revenueCatBillingDataSource.refreshCustomerInfo()
        return combine(
            revenueCatBillingDataSource.observeCustomerInfo(),
            userDataStore.totalSupportedAmountUSD
        ) { customerInfo, localTotalUSD ->
            val isMonthly = customerInfo.entitlements["monthly_supporter"]?.isActive == true ||
                    customerInfo.entitlements.active.isNotEmpty() ||
                    customerInfo.activeSubscriptions.isNotEmpty()

            // Save to DataStore automatically whenever RevenueCat status updates
            userDataStore.setIsSubscribed(isMonthly)

            CustomerSupportInfo(
                totalSupportedUSD = localTotalUSD,
                isMonthlySupporter = isMonthly,
                activeSubscriptionId = customerInfo.entitlements["monthly_supporter"]?.productIdentifier
                    ?: customerInfo.activeSubscriptions.firstOrNull()
            )
        }
    }

    override suspend fun fetchNativePackages(): Result<List<NativeSupportPackage>> {
        return try {
            val offerings = revenueCatBillingDataSource.getOfferings()
            val currentOffering = offerings.current
            Log.d(TAG, "Fetched offerings. Current offering: ${currentOffering?.identifier}, available packages count: ${currentOffering?.availablePackages?.size}")
            
            cachedRcPackages.clear()
            if (currentOffering != null && currentOffering.availablePackages.isNotEmpty()) {
                val nativePackages = currentOffering.availablePackages.map { pkg ->
                    Log.d(TAG, "Available Package: id=${pkg.identifier}, productId=${pkg.product.id}, price=${pkg.product.price.formatted}")
                    cachedRcPackages[pkg.identifier] = pkg
                    cachedRcPackages[pkg.product.id] = pkg
                    cachedRcPackages[pkg.identifier.lowercase()] = pkg
                    cachedRcPackages[pkg.product.id.lowercase()] = pkg

                    NativeSupportPackage(
                        packageId = pkg.identifier,
                        productId = pkg.product.id,
                        formattedPrice = pkg.product.price.formatted,
                        rawPriceMicros = pkg.product.price.amountMicros,
                        currencyCode = pkg.product.price.currencyCode
                    )
                }
                Result.success(nativePackages)
            } else {
                Log.w(TAG, "No offerings/packages found in RevenueCat dashboard. Make sure Offering is set to Current.")
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching native packages from RevenueCat", e)
            Result.failure(e)
        }
    }

    override suspend fun purchasePackage(
        activity: Activity,
        packageToPurchase: NativeSupportPackage
    ): Result<CustomerSupportInfo> {
        return try {
            // Check cached packages
            var rcPackage = cachedRcPackages[packageToPurchase.packageId]
                ?: cachedRcPackages[packageToPurchase.productId]
                ?: cachedRcPackages[packageToPurchase.packageId.lowercase()]
                ?: cachedRcPackages[packageToPurchase.productId.lowercase()]

            // Fetch fresh offerings if cache miss
            val offerings = revenueCatBillingDataSource.getOfferings()
            val availablePackages = offerings.current?.availablePackages.orEmpty()

            if (rcPackage == null && availablePackages.isNotEmpty()) {
                rcPackage = availablePackages.find { pkg ->
                    pkg.identifier.equals(packageToPurchase.packageId, ignoreCase = true) ||
                            pkg.product.id.equals(packageToPurchase.productId, ignoreCase = true) ||
                            pkg.product.id.contains(packageToPurchase.productId, ignoreCase = true) ||
                            packageToPurchase.productId.contains(pkg.product.id, ignoreCase = true)
                } ?: availablePackages.firstOrNull() // Flexible fallback to first available active package!
            }

            if (rcPackage == null) {
                val errorMsg = "No packages available in RevenueCat offering for '${packageToPurchase.productId}'."
                Log.e(TAG, errorMsg)
                return Result.failure(IllegalStateException(errorMsg))
            }

            Log.d(TAG, "Initiating RevenueCat purchase for Package: id=${rcPackage.identifier}, productId=${rcPackage.product.id}")
            val customerInfo = revenueCatBillingDataSource.purchasePackage(activity, rcPackage)
            
            val amountUSD = rcPackage.product.price.amountMicros / 1_000_000.0
            userDataStore.addSupportedAmountUSD(amountUSD)

            val isMonthly = customerInfo.entitlements["monthly_supporter"]?.isActive == true
            userDataStore.setIsSubscribed(isMonthly)

            Result.success(
                CustomerSupportInfo(
                    totalSupportedUSD = amountUSD,
                    isMonthlySupporter = isMonthly,
                    activeSubscriptionId = customerInfo.entitlements["monthly_supporter"]?.productIdentifier
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Purchase failed or cancelled: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun restorePurchases(): Result<CustomerSupportInfo> {
        return try {
            val customerInfo = revenueCatBillingDataSource.restorePurchases()
            val isMonthly = customerInfo.entitlements["monthly_supporter"]?.isActive == true
                    || customerInfo.entitlements.active.isNotEmpty()
                    || customerInfo.activeSubscriptions.isNotEmpty()

            userDataStore.setIsSubscribed(isMonthly)

            val totalFromTransactions = (customerInfo.nonSubscriptionTransactions.size * 2.99)
                .coerceAtLeast(if (isMonthly) 9.99 else 0.0)

            if (totalFromTransactions > 0.0) {
                userDataStore.setTotalSupportedAmountUSD(totalFromTransactions)
            }

            val activeSubId = customerInfo.entitlements["monthly_supporter"]?.productIdentifier
                ?: customerInfo.activeSubscriptions.firstOrNull()

            Result.success(
                CustomerSupportInfo(
                    totalSupportedUSD = totalFromTransactions,
                    isMonthlySupporter = isMonthly,
                    activeSubscriptionId = activeSubId
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Restore purchases failed", e)
            Result.failure(e)
        }
    }
}
