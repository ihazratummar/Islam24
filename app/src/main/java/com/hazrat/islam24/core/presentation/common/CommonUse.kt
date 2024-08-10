package com.hazrat.islam24.core.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity

@Composable
fun LocationName(locationDetailsEntity: LocationDetailsEntity) {
    Location(modifier = Modifier,locationDetailsEntity)
}


@Composable
fun Location(modifier: Modifier = Modifier,
    locationDetailsEntity: LocationDetailsEntity
) {

    Text(text = locationDetailsEntity.city?: locationDetailsEntity.village?: locationDetailsEntity.town ?: locationDetailsEntity.suburb?:"",
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.SemiBold
    )
}
