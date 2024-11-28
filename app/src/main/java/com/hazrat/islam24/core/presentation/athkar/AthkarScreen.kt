package com.hazrat.islam24.core.presentation.athkar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.AthkarDataEntity
import com.hazrat.islam24.core.presentation.athkar.components.AdhkarCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthkarScreen(
    athkar: List<AthkarDataEntity>,
    onBackClick:()-> Unit
) {

    Scaffold(modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.athkar),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    onBackClick()
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
    ) { paddingvalues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingvalues)
        ) {
            items(athkar) { athkar ->
                AdhkarCard(adhkars = athkar)
            }
        }
    }
}