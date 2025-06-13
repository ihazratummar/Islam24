package com.hazrat.islam24.core.presentation.common

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.presentation.home.component.shimmerEffect
import com.hazrat.islam24.ui.theme.dimens
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hazrat.islam24.R



@Composable
fun LocationOnCard(
    modifier: Modifier = Modifier,
    locationDetailsEntity: LocationDetailsEntity
) {

    Text(
        text = locationDetailsEntity.city ?: locationDetailsEntity.village
        ?: locationDetailsEntity.town ?: locationDetailsEntity.suburb ?: "",
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun OfflineCard(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimens.size10)
    ) {
        Text(
            text = "",
            modifier = Modifier
                .padding(dimens.size10)
                .size(dimens.size20)
                .shimmerEffect()
        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimens.size1)
        ) {
            Text(
                text = "",
                modifier = Modifier
                    .padding(dimens.size10)
                    .width(dimens.size150)
                    .size(dimens.size20)
                    .shimmerEffect()
            )
            Text(
                text = "",
                modifier = Modifier
                    .padding(dimens.size10)
                    .width(dimens.size80)
                    .size(dimens.size20)
                    .shimmerEffect()
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = "",
            modifier = Modifier
                .padding(dimens.size10)
                .width(dimens.size80)
                .size(dimens.size20)
                .shimmerEffect()
        )
    }
}


@Composable
fun rememberImageLoader(context: Context): ImageLoader {
    return remember {
        ImageLoader.Builder(context)
            .crossfade(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }.build()
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


@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    url: String
) {
    var backEnable by remember { mutableStateOf(false) }
    var webView: WebView? = null


    val loaderDialogScreen = remember { mutableStateOf(false) }

    if (loaderDialogScreen.value) {
        Dialog(
            onDismissRequest = { loaderDialogScreen.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator()
                }
            }
        }
    }

    AndroidView(
        modifier = Modifier,
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        backEnable = view!!.canGoBack()
                        loaderDialogScreen.value = true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        loaderDialogScreen.value = false
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(url)
                webView = this
            }
        }, update = {
            webView = it
        }
    )
    BackHandler(enabled = backEnable) {
        webView?.goBack()
    }
}

@Composable
fun BackIcon(
    onBackClick:() -> Unit,
    icon: Painter = painterResource(R.drawable.backicon)
) {
    Icon(
        painter = icon,
        contentDescription = null,
        modifier = Modifier.padding(dimens.size20)
            .clickable(
                onClick = {
                    onBackClick()
                }
            )
    )
}
