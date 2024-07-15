package com.hazrat.islam24.presentation.tasbih

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size10),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.tasbih),
                        color = colorResource(id = R.color.text),
                        style =  MaterialTheme.typography.displaySmall
                    )
                },
                navigationIcon = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        },
                        tint = colorResource(id = R.color.primary))
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
            )
        }
    ) {
        TasbihCounterApp(modifier = Modifier.padding(it))
    }
}