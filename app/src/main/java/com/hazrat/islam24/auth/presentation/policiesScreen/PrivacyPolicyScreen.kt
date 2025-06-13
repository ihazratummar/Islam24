package com.hazrat.islam24.auth.presentation.policiesScreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hazrat.islam24.core.presentation.common.BackIcon
import com.hazrat.islam24.core.presentation.common.WebViewScreen
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 15-03-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    modifier: Modifier = Modifier,
    onBackClick:() -> Unit
) {

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
                navigationIcon = {
                    BackIcon(
                        onBackClick = {onBackClick()}
                    )
                },
                windowInsets = WindowInsets(top = dimens.size20)
            )
        }
    ){ paddingValues ->
        WebViewScreen(
            modifier = modifier.fillMaxSize().padding(paddingValues),
            url = "https://sites.google.com/view/islam24-privacy-policy/home"
        )
    }

}