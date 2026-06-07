package com.hazrat.auth.ui.policiesScreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.WebViewScreen
import com.hazrat.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 15-03-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalScreens(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    url: String = "https://islam24.hazratdev.top/privacy-policy",
    title: String
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    BackIcon(
                        onBackClick = { onBackClick() }
                    )
                },
                windowInsets = WindowInsets(top = dimens.space20)
            )
        }
    ) { paddingValues ->
        WebViewScreen(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            url = url,
            hideHeaderFooter = true
        )
    }

}