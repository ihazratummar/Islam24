package com.hazrat.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

/**
 * Fast-as-light SVG Calligraphy Image renderer using Coil with memory caching and 0ms crossfade.
 *
 * @author hazratummar
 */
@Composable
fun SurahSvgImage(
    surahNumber: Int,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalContext.current
    val assetPath = remember(surahNumber) {
        SurahSvgProvider.getSvgAssetPath(surahNumber)
    }

    val imageRequest = remember(assetPath, context) {
        ImageRequest.Builder(context)
            .data(assetPath)
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(false)
            .build()
    }

    val colorFilter = remember(tint) {
        if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = "Surah $surahNumber",
        modifier = modifier,
        colorFilter = colorFilter,
        contentScale = contentScale
    )
}
