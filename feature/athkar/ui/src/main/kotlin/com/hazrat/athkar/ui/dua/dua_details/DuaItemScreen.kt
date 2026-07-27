package com.hazrat.athkar.ui.dua.dua_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens


/**
 * @author hazratummar
 * Created on 30/05/26
 */


private fun String.cleanUthmanic(): String {
    return this
        .replace("\u06DF", "")       // Remove Small High Rounded Zero (the small circle mark)
        .replace('\u0652', '\u06e1') // Convert standard round sukun to comma sukun
        .replace("\uFEFF", "")       // Remove BOM
        .trim()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaItemScreen(
    modifier: Modifier = Modifier,
    state: DuaItemState,
    onBackClick: () -> Unit
) {


    Scaffold(
        containerColor = Color(0xFF272727),
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(dimens.space4)
                    ) {
                        Text(
                            text = "",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "",
                            style = MaterialTheme.typography.bodySmall,
                            color = customColors.secondaryText
                        )
                    }
                },
                navigationIcon = {
                    BackIcon(
                        onBackClick = onBackClick
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(top = dimens.space20)
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->

        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.space20),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {

            items(state.duaItemLis) { dua ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimens.space12),
                    verticalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    // Aya Pill Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .padding(horizontal = dimens.space12, vertical = dimens.space4)
                    ) {
                        Text(
                            text = "${dua.id}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    val plainText = dua.arabicText.cleanUthmanic()
                    Text(
                        text = plainText,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = ScheherazadeFontFamily,
                            color = MaterialTheme.colorScheme.onBackground,
                            textDirection = TextDirection.Rtl,
                            fontFeatureSettings = "cv62",
                            fontSize = 30.sp,
                            lineHeight = 60.sp
                        )
                    )

                    // Translation
                    Text(
                        text = dua.translation,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.End
                        )
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(top = dimens.space12),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
                    )
                }
            }
        }
    }
}