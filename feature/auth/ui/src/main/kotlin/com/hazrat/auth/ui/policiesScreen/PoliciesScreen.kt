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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
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
                title = { Text("Policies") },
                navigationIcon = {
                    BackIcon(
                        onBackClick = {onBackClick()}
                    )
                },
                windowInsets = WindowInsets(top = dimens.space20)
            )
        }
    ) { paddingValues ->

        val listOfTabs = listOf(
            PoliciesTabs(
                leadingIcon = R.drawable.privacy_policy,
                tabName = "Privacy Policy",
                onClick = {
                    onPolicyClick("https://islam24.hazratdev.top/privacy-policy", "Privacy Policy")
                }
            ),
            PoliciesTabs(
                leadingIcon = R.drawable.privacy_policy,
                tabName = "Terms of Service",
                onClick = {
                    onPolicyClick("https://islam24.hazratdev.top/terms-of-service", "Terms of Service")
                }
            ),
            PoliciesTabs(
                leadingIcon = R.drawable.privacy_policy,
                tabName = "Acknowledgement",
                onClick = {
                    onPolicyClick("https://islam24.hazratdev.top/acknowledgements", "Acknowledgement")
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