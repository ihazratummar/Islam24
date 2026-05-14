package com.hazrat.athkar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hazrat.athkar.domain.model.AthkarData
import com.hazrat.athkar.ui.components.AdhkarCard
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthkarScreen(
    modifier: Modifier = Modifier,
    athkar: List<AthkarData>,
    onBackClick: () -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.space48))
            BasicTopBar(
                topBarTitle = stringResource(R.string.athkar),
                onBackClick = { onBackClick.invoke() }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(athkar) { athkar ->
                    AdhkarCard(adhkars = athkar)
                }
            }
        }
    }

}