package com.hazrat.athkar.ui.dua.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.hazrat.athkar.ui.dua.utils.DuaResourceMapper
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.ui.theme.dimens

/**
 * 2-Column Grid Item for Hisnul Muslim Categories.
 * @author hazratummar
 */
@Composable
fun DuaCategoryCard(
    category: HisnulMuslimCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.compCardMin)
            .clip(RoundedCornerShape(dimens.cornerLg))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space12),
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

            val isBengali = LocalLocale.current.platformLocale.language == "bn"
            val titleText = if (isBengali && category.bnTitle.isNotBlank()) category.bnTitle else category.title

            Text(
                text = titleText,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
