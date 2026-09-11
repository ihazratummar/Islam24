package com.hazrat.auth.ui.policiesScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hazrat.auth.ui.component.SettingItemCard
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-03-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoliciesScreen(
    modifier: Modifier = Modifier,
    onBackClick:() -> Unit,
    onPolicyClick:(String, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.common_policies)) },
                navigationIcon = {
                    BackIcon(
                        onBackClick = {onBackClick()}
                    )
                },
                windowInsets = WindowInsets(top = dimens.space20)
            )
        }
    ) { paddingValues ->

        val privacyPolicyTitle = stringResource(R.string.login_privacy_policy)
        val termsOfServiceTitle = stringResource(R.string.login_terms_of_service)
        val legalAcknowledgementTitle = stringResource(R.string.legal_acknowledgement)

        val listOfTabs = listOf(
            PoliciesTabs(
                leadingIcon = R.drawable.privacy_policy,
                tabName = privacyPolicyTitle,
                onClick = {
                    onPolicyClick("https://islam24.app/privacy-policy", privacyPolicyTitle)
                }
            ),
            PoliciesTabs(
                leadingIcon = R.drawable.privacy_policy,
                tabName = termsOfServiceTitle,
                onClick = {
                    onPolicyClick("https://islam24.app/terms-of-service", termsOfServiceTitle)
                }
            ),
            PoliciesTabs(
                leadingIcon = R.drawable.privacy_policy,
                tabName = legalAcknowledgementTitle,
                onClick = {
                    onPolicyClick("https://islam24.app/acknowledgements", legalAcknowledgementTitle)
                }
            )
        )
        Column (
            modifier = modifier.fillMaxSize().padding(horizontal = dimens.space20)
                .padding(paddingValues)
        ){
            Card (
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ){
                listOfTabs.forEachIndexed { index, data ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        SettingItemCard(
                            leadingIcon = data.leadingIcon,
                            settingText = data.tabName,
                            onClick = {data.onClick()},
                            trailingIcon = R.drawable.arrowright
                        )
                        if (index != listOfTabs.size - 1){
                            HorizontalDivider()
                        }
                    }
                }
            }
        }

    }

}


data class PoliciesTabs(
    val leadingIcon: Int,
    val tabName: String,
    val onClick: () -> Unit = {}
)