package com.hazrat.islam24.presentation.tasbih

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hazrat.islam24.ui.theme.Hidayat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.padding(horizontal = 10.dp),
        topBar = { TopAppBar(title = { Text(text = "Tasbih Count",
            fontFamily = Hidayat,
            color = Color.White)},
            navigationIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
            },
            colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)) }
    ) {
        TasbihCounterApp(modifier = Modifier.padding(it))
    }
}