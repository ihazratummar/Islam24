package com.hazrat.islam24.presentation.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.hazrat.islam24.R

data class Page(
    val text1: String?,
    val text2:String?,
    @DrawableRes val image: Int?,
)

val pages = listOf(
    Page(
        text1 = "PRAYER",
        text2 = "TIME",
        image = R.drawable.onboarding1,
    ),
    Page(
        text1 = "AL QUR'AN",
        text2 = "& DUA",
        image = R.drawable.onboarding2,
    ),
    Page(
        text1 = "ZAKAT",
        text2 = "CALCULATOR",
        image = R.drawable.onboarding3),

    Page(
        text1 = "QIBLA",
        text2 = "DIRECTION",
        image = R.drawable.onboarding4
    ),
    Page(
        text1 = "ISLAM 24",
        text2 = null,
        image = R.drawable.onboarding5
    )
)


