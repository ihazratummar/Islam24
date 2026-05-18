package com.hazrat.qibla.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.request.ImageRequest
import com.hazrat.model.qiblaModels.CompassModels
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 27-01-2025
 */

@Composable
fun BottomCompassPreview(
    modifier: Modifier = Modifier,
    onCompassClick: (Int, Boolean) -> Unit,

    ) {

    val context = LocalContext.current
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(compassList) { compass ->
            val compassImage = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context).data(compass.compassImage)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                imageLoader = context.imageLoader
            )
            Box(
                modifier = Modifier
                    .size(dimens.avatarXl)
                    .clickable(onClick = { onCompassClick(compass.id, compass.isLoggedInRequired) })
            ) {
                Image(
                    painter = compassImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimens.space8)
                )
            }
        }
    }
}


val compassList = listOf(
    CompassModels(
        id = 1,
        name = "Cyan Compass",
        compassImage = "file:///android_asset/compass/blue_compass.svg",
        compassNeedle = com.hazrat.ui.R.drawable.blue_needle,
        isLoggedInRequired = false,
        compassMiddle = com.hazrat.ui.R.drawable.blue_middle_compass
    ),
    CompassModels(
        id = 2,
        name = "Cyan Compass",
        compassImage = "file:///android_asset/compass/gold_compass.svg",
        compassNeedle = com.hazrat.ui.R.drawable.gold_niddle,
        isLoggedInRequired = true,
        compassMiddle = R.drawable.niddle_middle
    )
)