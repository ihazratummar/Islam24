package com.hazrat.islam24.core.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.ui.theme.Islam24Theme

@Composable
fun LocationName(locationDetailsEntity: LocationDetailsEntity) {

    Location(modifier = Modifier,locationDetailsEntity)
}


@Composable
fun Location(modifier: Modifier = Modifier,
    locationDetailsEntity: LocationDetailsEntity
) {
    Text(text = locationDetailsEntity.city?: locationDetailsEntity.village?: locationDetailsEntity.town ?: "",
        modifier = modifier,
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontWeight = FontWeight.SemiBold
    )
}

@Preview
@Composable
fun LocationNamePreview() {
    Islam24Theme {
//        LocationName()
    }
}