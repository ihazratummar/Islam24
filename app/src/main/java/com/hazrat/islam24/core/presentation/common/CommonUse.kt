package com.hazrat.islam24.core.presentation.common

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.glance.LocalContext
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.presentation.home.component.shimmerEffect
import com.hazrat.islam24.ui.theme.dimens

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
