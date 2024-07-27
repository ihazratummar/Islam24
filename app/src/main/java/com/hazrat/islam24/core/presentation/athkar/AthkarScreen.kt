package com.hazrat.islam24.core.presentation.athkar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.core.domain.model.athkar.tabItems
import com.hazrat.islam24.core.presentation.athkar.components.EveningContent
import com.hazrat.islam24.core.presentation.athkar.components.MorningContent
import com.hazrat.islam24.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AthkarScreen(navController: NavController) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    Scaffold(modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Dhikr & Athkar",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.backicon),
                        contentDescription = "Back"
                    )
                }
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    Color.Transparent
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent
            ) {
                tabItems.forEachIndexed { index, item ->
                    Tab(selected = index == selectedTabIndex,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = item.title, color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> MorningContent()
                    1 -> EveningContent()
                }
            }
        }
    }
}