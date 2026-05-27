package com.hazrat.ui.common


import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
                    .width(dimens.compCardMin)
                    .size(dimens.space20)
                    .shimmerEffect()
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = "",
            modifier = Modifier
                .padding(dimens.space12)
                .width(dimens.compCardMin)
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
    onBackClick: () -> Unit
) {

    IconWithBackground(
        icon = R.drawable.arrow_left,
        onClick = onBackClick,
        containerColor = MaterialTheme.colorScheme.onBackground.copy(0.1f)
    )
}



@Composable
fun TopAppBarTitle(
    title: Int = R.string.prayer_times
){
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}



@Composable
fun PulsingLiveDot(
    color: Color = Color(0xFF34D399), // emerald-400
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(dimens.space16)
    ) {
        // outer ping ring
        val infiniteTransition = rememberInfiniteTransition(label = "ping")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 2.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = EaseOut),
                repeatMode = RepeatMode.Restart
            ),
            label = "scale"
        )
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = EaseOut),
                repeatMode = RepeatMode.Restart
            ),
            label = "alpha"
        )

        Box(
            modifier = Modifier
                .size(dimens.space12)
                .scale(scale)
                .background(
                    color = color.copy(alpha = alpha),
                    shape = CircleShape
                )
        )

        // inner solid dot
        Box(
            modifier = Modifier
                .size(dimens.space8)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun IconWithBackground(
    modifier: Modifier = Modifier,
    icon: Int,
    containerColor: Color = MaterialTheme.colorScheme.outline,
    onClick: () -> Unit = {},
    iconColor: Color = MaterialTheme.colorScheme.onBackground
) {

    Box(
        modifier = modifier
            .size(dimens.space48)
            .padding(dimens.space4)
            .clip(RoundedCornerShape(dimens.space12))
            .background(color = containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .padding(dimens.space12)
                .size(dimens.iconSm),
            tint = iconColor
        )
    }
}


@Composable
fun LongText(
    text: String,
    maxLine: Int = 1,
    overflow: TextOverflow = TextOverflow.Visible,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    delay: Int = 5000,
    repeatDelay: Int = 5000
){
    Text(
        text = text,
        style = style,
        maxLines = maxLine,
        overflow = overflow,
        modifier = Modifier
            .fillMaxWidth()
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                animationMode = MarqueeAnimationMode.Immediately,
                initialDelayMillis = delay,
                repeatDelayMillis = repeatDelay,
                velocity = dimens.space32
            )
    )
}
