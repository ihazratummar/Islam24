package com.hazrat.athkar.ui.dua.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.athkar.ui.dua.utils.DuaResourceMapper
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.dimens

/**
 * Hero Banner Header for category detail screen with panoramic banner, 3D icon badge, and smooth gradient fade.
 * @author hazratummar
 */
@Composable
fun DuaCategoryDetailHeader(
    category: HisnulMuslimCategory,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.layoutXxl)
    ) {
        Image(
            painter = painterResource(id = DuaResourceMapper.getCategoryBanner(category)),
            contentDescription = category.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Black.copy(alpha = 0.5f),
                            0.5f to Color.Transparent,
                            0.85f to backgroundColor.copy(alpha = 0.9f),
                            1.0f to backgroundColor
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(horizontal = dimens.space8, vertical = dimens.space8)
        ) {
            BackIcon(
                onBackClick = onBackClick
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = dimens.space16, vertical = dimens.space12),
            horizontalArrangement = Arrangement.spacedBy(dimens.space12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = DuaResourceMapper.getCategoryIcon(category)),
                contentDescription = category.title,
                modifier = Modifier
                    .size(dimens.iconXl)
                    .clip(RoundedCornerShape(dimens.cornerMd)),
                contentScale = ContentScale.Crop
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = category.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }
        }
    }
}
