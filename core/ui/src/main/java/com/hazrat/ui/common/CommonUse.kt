package com.hazrat.ui.common


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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import com.hazrat.ui.R
import com.hazrat.ui.shimmerEffect
import com.hazrat.ui.theme.dimens

@Composable
fun LocationOnCard(
    modifier: Modifier = Modifier,
    locationName: String,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {

    Text(
        text = locationName,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = textColor,
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
            .padding(dimens.space12)
    ) {
        Text(
            text = "",
            modifier = Modifier
                .padding(dimens.space12)
                .size(dimens.space20)
                .shimmerEffect()
        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimens.space2)
        ) {
            Text(
                text = "",
                modifier = Modifier
                    .padding(dimens.space12)
                    .width(150.dp)
                    .size(dimens.space20)
                    .shimmerEffect()
            )
            Text(
                text = "",
                modifier = Modifier
                    .padding(dimens.space12)
                    .width(dimens.compCard)
                    .size(dimens.space20)
                    .shimmerEffect()
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = "",
            modifier = Modifier
                .padding(dimens.space12)
                .width(dimens.compCard)
                .size(dimens.space20)
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
        modifier = Modifier.padding(dimens.space4)
            .clickable(
                onClick = {
                    onBackClick()
                }
            )
    )
}
