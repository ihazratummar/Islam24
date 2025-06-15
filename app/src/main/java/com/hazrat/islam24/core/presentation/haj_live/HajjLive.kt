package com.hazrat.islam24.core.presentation.haj_live

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hazrat.islam24.core.domain.model.hajjlive.HajjLiveYoutubeModel
import com.hazrat.ui.R
import com.hazrat.islam24.core.presentation.common.YoutubePlayer
import com.hazrat.ui.theme.dimens


/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-03-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HajjLive(
    modifier: Modifier = Modifier,
    hajjLiveYoutubeModel: HajjLiveYoutubeModel?,
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hajj Live") },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.padding(dimens.size10).clickable(
                            onClick = {onBackClick()}
                        ),
                        painter = painterResource(R.drawable.backicon),
                        contentDescription = null
                    )
                },
                windowInsets = WindowInsets(top = dimens.size20),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            hajjLiveYoutubeModel?.items?.firstOrNull()?.let {
                YoutubePlayer(
                    youtubeVideoId = it.id.videoId,
                    lifecycleOwner = LocalLifecycleOwner.current
                )
            }
        }
    }

}