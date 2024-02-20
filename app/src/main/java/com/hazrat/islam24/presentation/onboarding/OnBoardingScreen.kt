package com.hazrat.islam24.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.hazrat.islam24.presentation.Dimens.Padding30
import com.hazrat.islam24.presentation.Dimens.Padding50
import com.hazrat.islam24.presentation.Dimens.Size10
import com.hazrat.islam24.presentation.Dimens.Size30
import com.hazrat.islam24.presentation.Dimens.Size50
import com.hazrat.islam24.presentation.common.ContinueButton
import com.hazrat.islam24.presentation.common.HyperLinkText
import com.hazrat.islam24.presentation.onboarding.components.OnBoardingPage
import com.hazrat.islam24.ui.theme.Islam24Theme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    onEvent: (OnBoardingEvent) -> Unit
) {
    val pagerState  = rememberPagerState {
        pages.size
    }

    Box(modifier = Modifier
        .fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxSize()
            .zIndex(2f)
            .padding(start = Size30, end = Size30, bottom = Size50)
            .navigationBarsPadding(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Column() {
                val buttonState = remember {
                    derivedStateOf {
                        when(pagerState.currentPage){
                            0, 1, 2, 3 -> listOf("", "")
                            4 -> listOf("", "CONTINUE")
                            else -> listOf("", "")
                        }
                    }
                }
                val scope = rememberCoroutineScope()
                if (buttonState.value[1].isNotEmpty()){
                    ContinueButton(buttonText = buttonState.value[1],
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage == 4){
                                    onEvent(OnBoardingEvent.SaveAppEntry)
                                }else{
                                    pagerState.animateScrollToPage(
                                        page = pagerState.currentPage +1
                                    )
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(Size10))
                    HyperLinkText(fullText = "By tapping 'Continue' you agree to our Terms and  Conditions and Privacy Policy",
                        linkText = listOf("Terms and  Conditions", "Privacy Policy"),
                        hyperlinks = listOf("https:www.google.com", "https:www.google.com") )
                }
            }
        }
        VerticalPager(state = pagerState) {index ->
            OnBoardingPage(page = pages[index])
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun OnBoardingScreenPreview(){
    Islam24Theme {
        OnBoardingScreen(onEvent = {})
    }
}