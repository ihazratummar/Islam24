package com.hazrat.islam24.auth.presentation.policiesScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.presentation.appSetting.SettingItemCard
import com.hazrat.islam24.core.presentation.common.BackIcon
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-03-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoliciesScreen(
    modifier: Modifier = Modifier,
    onBackClick:() -> Unit,
    onPolicyClick:() -> Unit
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
                windowInsets = WindowInsets(top = dimens.size20)
            )
        }
    ) { paddingValues ->

        val listOfTabs = listOf<PoliciesTabs>(
            PoliciesTabs(
                leadingIcon = painterResource(R.drawable.privacy_policy),
                tabName = "Privacy Policy",
                onClick = {
                    onPolicyClick()
                }
            )
        )
        Column (
            modifier = modifier.fillMaxSize().padding(horizontal = dimens.size20)
                .padding(paddingValues)
        ){
            Card (
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ){
                listOfTabs.forEach {
                    SettingItemCard(
                        leadingIcon = it.leadingIcon,
                        text = it.tabName,
                        onClick = {it.onClick()}
                    )
                }
            }
        }

    }

}


data class PoliciesTabs(
    val leadingIcon: Painter,
    val tabName: String,
    val onClick: () -> Unit = {}
)