package com.hazrat.qibla.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.request.ImageRequest
import com.hazrat.model.qiblaModels.CompassModels
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

@Composable
fun BottomCompassPreview(
    selectedCompassId: Int = 1,
    onCompassClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.space12, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(horizontal = dimens.space16)
    ) {
        items(compassList) { compass ->
            val isSelected = compass.id == selectedCompassId
            val compassImage = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context).data(compass.compassImage)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                imageLoader = context.imageLoader
            )
            Box(
                modifier = Modifier
                    .size(dimens.avatarLg)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        else Color.Transparent
                    )
                    .border(
                        width = if (isSelected) dimens.space2 else dimens.divider,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .clickable { onCompassClick(compass.id) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = compassImage,
                    contentDescription = if (compass.id == 1) stringResource(R.string.qibla_cyan_compass) else stringResource(R.string.qibla_gold_compass),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimens.space4)
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
        compassNeedle = R.drawable.blue_needle,
        isLoggedInRequired = false,
        compassMiddle = R.drawable.blue_middle_compass
    ),
    CompassModels(
        id = 2,
        name = "Gold Compass",
        compassImage = "file:///android_asset/compass/gold_compass.svg",
        compassNeedle = R.drawable.gold_niddle,
        isLoggedInRequired = false,
        compassMiddle = R.drawable.niddle_middle
    )
)