package com.hazrat.auth.ui.support

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.domain.repository.BillingRepository
import com.hazrat.domain.repository.NativeSupportPackage
import com.hazrat.model.profile.SupporterTickerModel
import com.hazrat.usecase.profile.ListenToSupportTickerUseCase
import com.hazrat.utils.network.ConnectivityObserver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

import com.hazrat.usecase.profile.GetSupporterStatusUseCase
import com.hazrat.usecase.profile.SyncSupporterStatusUseCase
import com.hazrat.utils.toCurrencySymbol

class SupportViewModel(
    private val billingRepository: BillingRepository? = null,
    private val connectivityObserver: ConnectivityObserver? = null,
    private val listenToSupportTickerUseCase: ListenToSupportTickerUseCase,
    private val getSupporterStatusUseCase: GetSupporterStatusUseCase,
    private val syncSupporterStatusUseCase: SyncSupporterStatusUseCase? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(SupportUiState())
    val uiState: StateFlow<SupportUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SupportEffect>()
    val effect: SharedFlow<SupportEffect> = _effect.asSharedFlow()

    private val _liveTicket = MutableStateFlow<SupporterTickerModel?>(null)
    val liveTicket: StateFlow<SupporterTickerModel?> = _liveTicket.asStateFlow()

    init {
        observeConnectivity()
        observeCustomerSupportInfo()
        observeSupporterStatus()
        loadUserSubStatsFromDb()
        loadNativeOfferings()

        viewModelScope.launch {
            listenToSupportTickerUseCase().collectLatest { ticker ->
                _liveTicket.value = ticker
                val type = if (ticker.type == "TIP") "Tip" else "Sub"
                _effect.emit(SupportEffect.Success("${ticker.donorName} $type ${ticker.currency?.toCurrencySymbol()}${ticker.amount}"))
            }
        }
    }


    private fun loadUserSubStatsFromDb(){
        viewModelScope.launch {
            getSupporterStatusUseCase.invoke().collectLatest { statusModel ->
                _uiState.update {
                    it.copy(
                        userSupportViewModel = statusModel
                    )
                }
            }
        }
    }

    private fun observeSupporterStatus() {
        viewModelScope.launch {
            syncSupporterStatusUseCase?.invoke()
        }
    }

    private fun observeConnectivity() {
        connectivityObserver?.observer()
            ?.onEach { status ->
                val isOffline = status != ConnectivityObserver.Status.Available
                _uiState.update { currentState ->
                    currentState.copy(isOffline = isOffline)
                }
                if (!isOffline) {
                    loadNativeOfferings()
                }
            }
            ?.catch { /* Handle network observation error */ }
            ?.launchIn(viewModelScope)
    }

    private fun observeCustomerSupportInfo() {
        billingRepository?.observeCustomerSupportInfo()
            ?.onEach { info ->
                val formattedTotal = formatRegionalCurrency(
                    amountUSD = info.totalSupportedUSD,
                    nativePackages = _uiState.value.nativePackages
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        totalSupportedUSD = info.totalSupportedUSD,
                        formattedSupportedTotal = formattedTotal,
                    )
                }
            }
            ?.catch { /* Handle error gracefully */ }
            ?.launchIn(viewModelScope)
    }

    private fun loadNativeOfferings() {
        viewModelScope.launch {
            billingRepository?.fetchNativePackages()?.onSuccess { nativePackages ->
                if (nativePackages.isNotEmpty()) {
                    val formattedTotal = formatRegionalCurrency(
                        amountUSD = _uiState.value.totalSupportedUSD,
                        nativePackages = nativePackages
                    )
                    _uiState.update { currentState ->
                        currentState.copy(
                            nativePackages = nativePackages,
                            formattedSupportedTotal = formattedTotal
                        )
                    }
                }
            }
        }
    }

    private fun formatRegionalCurrency(
        amountUSD: Double,
        nativePackages: List<NativeSupportPackage>
    ): String {
        return try {
            val currencyCode = nativePackages.firstOrNull()?.currencyCode
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            if (!currencyCode.isNull_orEmpty()) {
                try {
                    formatter.currency = Currency.getInstance(currencyCode)
                } catch (e: Exception) {
                    // Fallback to default currency symbol if code invalid
                }
            }
            val formatted = formatter.format(amountUSD)
            if (formatted.contains("¤")) {
                // If locale produces generic currency symbol ¤, replace with $ or explicit symbol
                formatted.replace("¤", "$")
            } else {
                formatted
            }
        } catch (e: Exception) {
            String.format(Locale.getDefault(), "$%.2f", amountUSD)
        }
    }

    private fun String?.isNull_orEmpty(): Boolean = this == null || this.trim().isEmpty()

    fun onEvent(event: SupportUiEvent) {
        when (event) {
            is SupportUiEvent.SelectTab -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedTab = event.tab,
                        selectedTipTier = if (event.tab == SupportTab.ONE_TIME) (currentState.selectedTipTier
                            ?: TipTier.SMALL) else null,
                        selectedSubscriptionTier = if (event.tab == SupportTab.MONTHLY) (currentState.selectedSubscriptionTier
                            ?: SubscriptionTier.SUPPORTER) else null
                    )
                }
            }

            is SupportUiEvent.SelectTipTier -> {
                _uiState.update { it.copy(selectedTipTier = event.tier) }
            }

            is SupportUiEvent.SelectSubscriptionTier -> {
                _uiState.update { it.copy(selectedSubscriptionTier = event.tier) }
            }

            is SupportUiEvent.ToggleFaq -> {
                _uiState.update { currentState ->
                    val newIndex =
                        if (currentState.expandedFaqIndex == event.index) null else event.index
                    currentState.copy(expandedFaqIndex = newIndex)
                }
            }

            is SupportUiEvent.PurchaseCurrentSelection -> {
                if (_uiState.value.isOffline) {
                    Toast.makeText(
                        event.activity,
                        "Please connect to internet to complete purchase",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                val currentTab = _uiState.value.selectedTab
                viewModelScope.launch {
                    _uiState.update { it.copy(isPurchasing = true) }

                    val targetProductId = if (currentTab == SupportTab.ONE_TIME) {
                        (_uiState.value.selectedTipTier ?: TipTier.SMALL).productId
                    } else {
                        (_uiState.value.selectedSubscriptionTier
                            ?: SubscriptionTier.SUPPORTER).productId
                    }

                    // Find authentic native package by productId, packageId, or fallback to ordinal match
                    val packages = _uiState.value.nativePackages
                    val targetOrdinal = if (currentTab == SupportTab.ONE_TIME) {
                        (_uiState.value.selectedTipTier ?: TipTier.SMALL).ordinal
                    } else {
                        (_uiState.value.selectedSubscriptionTier
                            ?: SubscriptionTier.SUPPORTER).ordinal
                    }

                    val authenticPackage = packages.find { pkg ->
                        pkg.productId.equals(targetProductId, ignoreCase = true) ||
                                pkg.packageId.equals(targetProductId, ignoreCase = true) ||
                                pkg.productId.contains(targetProductId, ignoreCase = true) ||
                                targetProductId.contains(pkg.productId, ignoreCase = true)
                    } ?: packages.getOrNull(targetOrdinal) ?: packages.firstOrNull()

                    val nativePackage = authenticPackage ?: if (currentTab == SupportTab.ONE_TIME) {
                        val tier = _uiState.value.selectedTipTier ?: TipTier.SMALL
                        val amountMicros = when (tier) {
                            TipTier.SMALL -> 990_000L
                            TipTier.GENEROUS -> 2_990_000L
                            TipTier.SUPPORTER -> 4_990_000L
                            TipTier.PATRON -> 9_990_000L
                            TipTier.CHAMPION -> 19_990_000L
                        }
                        NativeSupportPackage(
                            packageId = tier.name.lowercase(),
                            productId = tier.productId,
                            formattedPrice = tier.defaultPriceText,
                            rawPriceMicros = amountMicros,
                            currencyCode = "USD"
                        )
                    } else {
                        val tier =
                            _uiState.value.selectedSubscriptionTier ?: SubscriptionTier.SUPPORTER
                        val amountMicros = when (tier) {
                            SubscriptionTier.SUPPORTER -> 2_990_000L
                            SubscriptionTier.GUARDIAN -> 4_990_000L
                            SubscriptionTier.PATRON -> 9_990_000L
                        }
                        NativeSupportPackage(
                            packageId = tier.productId,
                            productId = tier.productId,
                            formattedPrice = tier.defaultPriceText,
                            rawPriceMicros = amountMicros,
                            currencyCode = "USD"
                        )
                    }

                    if (billingRepository == null) {
                        Toast.makeText(
                            event.activity,
                            "Billing service not initialized",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val result =
                            billingRepository.purchasePackage(event.activity, nativePackage)
                        result.onSuccess {
                            Toast.makeText(
                                event.activity,
                                "JazakAllah Khair for your support!",
                                Toast.LENGTH_LONG
                            ).show()
                        }.onFailure { error ->
                            val msg = error.message.orEmpty()
                            if (!msg.contains("cancelled", ignoreCase = true)) {
                                Toast.makeText(event.activity, "Support: $msg", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }

                    _uiState.update { it.copy(isPurchasing = false) }
                }
            }

            SupportUiEvent.RestorePurchases -> {
                viewModelScope.launch {
                    billingRepository?.restorePurchases()
                }
            }
        }
    }
}
