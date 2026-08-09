package com.hazrat.auth.data.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.hazrat.auth.data.BuildConfig
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.interfaces.UpdatedCustomerInfoListener
import com.revenuecat.purchases.purchaseWith
import com.revenuecat.purchases.restorePurchasesWith
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Data Source encapsulating RevenueCat SDK & Google Play Billing operations.
 * Auto-consumes one-time tip purchases so users can tip repeatedly.
 */
class RevenueCatBillingDataSource(
    private val context: Context
) {

    companion object {
        private const val TAG = "RevenueCatBilling"
        // RevenueCat Google Play API Key
        private val REVENUECAT_API_KEY = BuildConfig.REVENUECAT_API_KEY
    }

    init {
        try {
            if (!Purchases.isConfigured) {
                val configuration = PurchasesConfiguration.Builder(
                    context = context,
                    apiKey = REVENUECAT_API_KEY
                ).build()
                Purchases.configure(configuration)
                Log.d(TAG, "RevenueCat configured successfully with API Key")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to configure RevenueCat", e)
        }
    }

    /**
     * Observes real-time CustomerInfo updates from RevenueCat SDK.
     */
    fun observeCustomerInfo(): Flow<CustomerInfo> = callbackFlow {
        if (!Purchases.isConfigured) {
            close()
            return@callbackFlow
        }

        Purchases.sharedInstance.updatedCustomerInfoListener = UpdatedCustomerInfoListener { customerInfo ->
            trySend(customerInfo)
        }

        Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
            override fun onReceived(customerInfo: CustomerInfo) {
                trySend(customerInfo)
            }
            override fun onError(error: PurchasesError) {
                Log.e(TAG, "Error fetching initial customer info: ${error.message}, code=${error.code}")
            }
        })

        awaitClose {
            Purchases.sharedInstance.updatedCustomerInfoListener = null
        }
    }

    /**
     * Explicitly refreshes customer info from RevenueCat / Google Play.
     */
    fun refreshCustomerInfo() {
        if (!Purchases.isConfigured) return
        Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
            override fun onReceived(customerInfo: CustomerInfo) {
                Log.d(TAG, "Customer info refreshed. Active subscriptions: ${customerInfo.activeSubscriptions}")
            }
            override fun onError(error: PurchasesError) {
                Log.e(TAG, "Error refreshing customer info: ${error.message}")
            }
        })
    }

    /**
     * Fetches current offerings from RevenueCat & Google Play Store.
     */
    suspend fun getOfferings(): Offerings = suspendCancellableCoroutine { continuation ->
        if (!Purchases.isConfigured) {
            continuation.resumeWithException(IllegalStateException("RevenueCat is not configured"))
            return@suspendCancellableCoroutine
        }

        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: Offerings) {
                continuation.resume(offerings)
            }

            override fun onError(error: PurchasesError) {
                Log.e(TAG, "getOfferings error: ${error.message}, code=${error.code}, underlying=${error.underlyingErrorMessage}")
                continuation.resumeWithException(Exception(error.message))
            }
        })
    }

    /**
     * Purchases a native Package (consumable tip or monthly subscription).
     * Uses SubscriptionOption for subscriptions and Package for one-time tips.
     */
    suspend fun purchasePackage(
        activity: Activity,
        packageToPurchase: Package
    ): CustomerInfo = suspendCancellableCoroutine { continuation ->
        if (!Purchases.isConfigured) {
            continuation.resumeWithException(IllegalStateException("RevenueCat is not configured"))
            return@suspendCancellableCoroutine
        }

        val builder = packageToPurchase.product.defaultOption?.let { defaultOption ->
            Log.d(TAG, "Building PurchaseParams with defaultOption for subscription: ${defaultOption.id}")
            PurchaseParams.Builder(activity, defaultOption)
        } ?: run {
            Log.d(TAG, "Building PurchaseParams directly with Package: ${packageToPurchase.identifier}")
            PurchaseParams.Builder(activity, packageToPurchase)
        }

        val params = builder.build()
        Purchases.sharedInstance.purchaseWith(
            purchaseParams = params,
            onError = { error, userCancelled ->
                Log.e(TAG, "purchaseWith error: code=${error.code}, message=${error.message}, underlying=${error.underlyingErrorMessage}, userCancelled=$userCancelled")
                if (!userCancelled) {
                    val fullError = error.underlyingErrorMessage ?: error.message
                    continuation.resumeWithException(Exception(fullError))
                } else {
                    continuation.resumeWithException(Exception("Purchase cancelled by user"))
                }
            },
            onSuccess = { _, customerInfo ->
                Log.d(TAG, "purchaseWith success!")
                continuation.resume(customerInfo)
            }
        )
    }

    /**
     * Restores purchases via Google Play receipts.
     */
    suspend fun restorePurchases(): CustomerInfo = suspendCancellableCoroutine { continuation ->
        if (!Purchases.isConfigured) {
            continuation.resumeWithException(IllegalStateException("RevenueCat is not configured"))
            return@suspendCancellableCoroutine
        }

        Purchases.sharedInstance.restorePurchasesWith(
            onError = { error ->
                Log.e(TAG, "restorePurchases error: ${error.message}, code=${error.code}")
                continuation.resumeWithException(Exception(error.message))
            },
            onSuccess = { customerInfo ->
                continuation.resume(customerInfo)
            }
        )
    }
}
