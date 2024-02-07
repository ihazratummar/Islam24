package com.hazrat.islam24.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.islam24.ui.theme.Islam24Theme

@Composable
fun HomeScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .background(MaterialTheme.colorScheme.background)) {
        Text(text = "hello", style = MaterialTheme.typography.headlineLarge, color = Color.White)
    }
}


@Preview()
@Composable
fun HomeScreenPreview(){
    Islam24Theme {
        HomeScreen()
    }
}

