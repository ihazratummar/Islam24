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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.hazrat.islam24.R
import com.hazrat.islam24.ui.theme.Lafadz
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.getSystemLanguage


/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(
    modifier: Modifier = Modifier,
    onSurahClick: (Int) -> Unit,
    quranState: QuranState,
    onLastReadClick: (Int) -> Unit,
    refresh: () -> Unit
) {

    val quranBn = quranState.quranBnData

    val systemLanguage = getSystemLanguage()

    LaunchedEffect(quranState) {
        refresh()
        Log.d(
            "QuranViewModel",
            "QuranScreen: State updated to: ${quranState.lastReadSurah} ${quranState.lastReadAyah}"
        )
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )



    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                windowInsets = WindowInsets(top = dimens.size20, bottom = dimens.size5),
                title = {
                    Row(
                        modifier = Modifier.padding(vertical = dimens.size10),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Al-Quran",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Spacer(Modifier.width(dimens.size2))
                        Text(
                            text = "L",
                            fontFamily = Lafadz,
                            fontSize = 50.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimens.size20)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    Log.d(
                                        "QuranViewModel",
                                        "Quran Screen Surah Number ${quranState.lastReadSurah}"
                                    )
                                    onLastReadClick(quranState.lastReadSurah)
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
                                Text(quranState.lastReadSurahName ?: "Surah Name")
                                Text("Ayah ${quranState.lastReadAyah + 1}")
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
                if (quranState.arQuranData != null) {
                    items(quranState.arQuranData) {

                        val svgPath = "surah_icons/Surah=sname${it.number}.svg"


                        SurahCard(
                            surahNumber = it.number,
                            surahName = it.englishName,
                            surahVersesCount = it.ayahs.size,
                            type = it.revelationType,
                            onSurahClick = {
                                Log.d("QuranSurahClick", "QuranScreen: $it")
                                onSurahClick.invoke(it.number)
                            },
                            surahNameTranslation = when (systemLanguage) {
                                "bn" -> quranBn?.get(it.number - 1)?.translation ?: ""
                                else -> it.englishNameTranslation
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SurahCard(
    surahNumber: Int,
    surahName: String,
    surahNameTranslation: String,
    surahVersesCount: Int,
    type: String,
    modifier: Modifier = Modifier,
    onSurahClick: () -> Unit
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
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimens.size10),
        onClick = {
            onSurahClick.invoke()
        },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.size20, horizontal = dimens.size5),
            verticalAlignment = Alignment.CenterVertically
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
            Spacer(Modifier.width(dimens.size5))
            Column(
                modifier = Modifier.padding(dimens.size10),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
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
            Column(
                modifier = Modifier.padding(dimens.size10),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
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
                    modifier = Modifier.size(dimens.size100)
                )
                Text(
                    "Verses: $surahVersesCount",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
    }
}