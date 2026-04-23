package com.hazrat.hajjlive.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hazrat.model.hajjlive.HajjLiveYoutubeModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


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
            hajjLiveYoutubeModel?.let {
                YoutubePlayer(
                    youtubeVideoId = it.videoId,
                    lifecycleOwner = LocalLifecycleOwner.current
                )
            }
        }
    }

}


@Composable
fun YoutubePlayer(
    youtubeVideoId: String,
    lifecycleOwner: LifecycleOwner
) {

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.size20)
            .clip(RoundedCornerShape(dimens.size15)),
        factory = { context1 ->
            YouTubePlayerView(context = context1).apply {
                lifecycleOwner.lifecycle.addObserver(this)
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(youtubeVideoId, 0f)
                    }
                }
                )
            }
        }
    )
}