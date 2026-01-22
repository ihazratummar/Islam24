package com.hazrat.islam24.core.presentation.al_quran

import android.content.Context
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.request.ImageRequest
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

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

    val context = LocalContext.current
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
                    Text(text = tabTitle.getLocalizedTitle(context))
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
    lastReadSurahName: String?,
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
                    .padding(top = dimens.size10, start = dimens.size20),
                verticalAlignment = Alignment.CenterVertically
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
                Text(lastReadSurahName?: stringResource(R.string.start_reading))
                if (lastReadSurahName != null){
                    Text("${stringResource(R.string.ayah)} ${lastReadAyah.plus(1)}" )
                }
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
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Text(
                    text = "${stringResource(R.string.ayah)} $surahVersesCount",
                    style = MaterialTheme.typography.labelMedium.copy(
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
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Black,
                    )
                )
                Text(
                    text = surahNameTranslation,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Thin
                    )
                )
                Text(
                    type.uppercase(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Thin
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
                imageLoader = context.imageLoader,
                onSuccess = { result ->
                    Log.d("SVG Load", "SVG loaded successfully: ${result.result.request.data}")
                },
                onError = { error ->
                    Log.e("SVG Load", "Failed to load SVG: ${error.result.throwable}")
                },
                contentDescription = "Surah Calligraphy",
                modifier = Modifier.size(dimens.size80),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}


@Composable
fun JuzCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    index: Int,
    ayat: String
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = dimens.size10),
        onClick = {
            onClick()
        },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.size4)
        ){
            Text(
                text = "${stringResource(R.string.juz)} ${ index + 1}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(dimens.size5)
            )
            Text(
                text = "${stringResource(R.string.ayah)} $ayat",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(dimens.size5)
            )
        }
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

enum class QuranScreenTab(val titleResId: Int) {
    Surah(R.string.surah),
    Juz(R.string.juz),
    Favorite(R.string.favorite);
}

fun QuranScreenTab.getLocalizedTitle(context: Context): String {
    return context.getString(titleResId)
}
