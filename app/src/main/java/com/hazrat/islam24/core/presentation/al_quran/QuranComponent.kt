package com.hazrat.islam24.core.presentation.al_quran

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.hazrat.islam24.R
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 15-12-2024
 */


@Composable
fun TabRowComponent(
    modifier: Modifier = Modifier,
    contentScreens: List<@Composable () -> Unit>,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    indicationColor: Color = MaterialTheme.colorScheme.primary,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            contentColor = contentColor,
            indicator = { tabPosition ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPosition[selectedTabIndex]),
                    color = indicationColor
                )
            }
        ) {
            QuranScreenTab.entries.forEachIndexed { index, tabTitle ->
                Tab(
                    modifier = Modifier.padding(all = dimens.size15),
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) }
                ) {
                    Text(text = tabTitle.toString())
                }
            }
        }

        // Only invoke content for the selected tab
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            contentScreens.getOrNull(selectedTabIndex)?.invoke()
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LastReadCard(
    modifier: Modifier = Modifier,
    onLastReadClick: () -> Unit,
    lastReadSurahName: String,
    lastReadAyah: Int

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onLastReadClick()
                }
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF93FAD6),
                        Color(0xFF72FDA0)
                    )
                ),
                shape = RoundedCornerShape(dimens.size10)
            )
            .height(dimens.size150),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = dimens.size10, start = dimens.size20)
            ) {
                Icon(
                    painter = painterResource(R.drawable.book),
                    contentDescription = null
                )
                Spacer(Modifier.width(dimens.size10))
                Text(stringResource(R.string.last_read))
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = dimens.size10, start = dimens.size20)
            ) {
                Text(lastReadSurahName)
                Text("Ayah ${lastReadAyah + 1}")
            }
            Image(
                painter = painterResource(R.drawable.quran_image),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            )
        }
    }
}


@Composable
fun SurahCard(
    surahNumber: Int,
    surahName: String,
    surahNameTranslation: String,
    surahVersesCount: Int,
    type: String,
    modifier: Modifier = Modifier,
    onSurahClick: () -> Unit,
    isLastReadSurah: Boolean
) {

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .crossfade(true) // Optional, for crossfade effect
        .memoryCachePolicy(CachePolicy.ENABLED) // Enable in-memory cache
        .diskCachePolicy(CachePolicy.ENABLED) // Enable disk cache
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02) // Cache size as a percentage of available storage
                .build()
        }
        .build()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimens.size5),
        shape = RoundedCornerShape(dimens.size10),
        onClick = {
            onSurahClick.invoke()
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isLastReadSurah) MaterialTheme.colorScheme.secondaryContainer.copy(
                0.4f
            ) else Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.size5),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.size60)
                        .padding(vertical = dimens.size10, horizontal = dimens.size5),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.surahnumbericon),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = surahNumber.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Text(
                    text = "Verses: $surahVersesCount",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Thin
                    )
                )

            }
            Spacer(Modifier.width(dimens.size5))
            Column(
                modifier = Modifier
                    .padding(dimens.size10),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    surahName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Black,
                    )
                )
                Text(
                    text = surahNameTranslation,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Thin
                    )
                )
                Text(
                    type.uppercase(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            Spacer(Modifier.weight(1f))
            val fullPath = "file:///android_asset/surah_icons/Surah=sname_${surahNumber}.svg"
            Log.d("SVG Load Path", "Loading SVG from: $fullPath")
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(fullPath)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                imageLoader = imageLoader,
                onSuccess = { result ->
                    Log.d("SVG Load", "SVG loaded successfully: ${result.result.request.data}")
                },
                onError = { error ->
                    Log.e("SVG Load", "Failed to load SVG: ${error.result.throwable}")
                },
                contentDescription = "Surah Calligraphy",
                modifier = Modifier.size(dimens.size80)
            )
        }
    }
}


@Composable
fun JuzCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    index: Int
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onClick()
        },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Text(
            text = "Juz ${index + 1}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(dimens.size5)
        )
    }
}


@Composable
fun FavoriteAyahCard(
    modifier: Modifier = Modifier,
    translationText: String,
    surahAyahText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimens.size10),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = translationText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1, // Limit the text to one line
                overflow = TextOverflow.Ellipsis // Add ellipsis if the text overflows
            )
            Spacer(Modifier.height(dimens.size5))
            Text(
                text = surahAyahText,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer(Modifier.height(dimens.size5))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(0.4f),
                thickness = dimens.size1
            )
        }
    }
}

enum class QuranScreenTab {
    Surah {
        override fun toString() = "Surah"
    },
    Juz {
        override fun toString() = "Juz"
    },
    FAVORITE {
        override fun toString() = "Favorite"
    }
}