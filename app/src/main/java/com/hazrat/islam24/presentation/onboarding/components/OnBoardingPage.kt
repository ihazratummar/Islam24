package com.hazrat.islam24.presentation.onboarding.components


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.hazrat.islam24.presentation.onboarding.Page
import com.hazrat.islam24.presentation.onboarding.pages
import com.hazrat.islam24.ui.theme.Islam24Theme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingPage(
    modifier: Modifier =Modifier,
    page: Page
) {
    val pagerState = rememberPagerState(initialPage = 0) {
        pages.size
    }
    val isLastPage = pagerState.currentPage == pagerState.pageCount - 1

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                page.text1?.let {
                    Text(
                        modifier = Modifier,
                        text =
                        it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                page.text2?.let {
                    Text(
                        modifier = Modifier,
                        text = it,
                        color = Color.White,
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
        page.image?.let { painterResource(id = it) }?.let {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = it, contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    Islam24Theme {
        OnBoardingPage(page = pages[4]
        )
    }
}