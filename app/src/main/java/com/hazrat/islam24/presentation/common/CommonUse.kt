package com.hazrat.islam24.presentation.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.islam24.data.location.locationdetails.LocationDetailsEntity
import com.hazrat.islam24.presentation.Dimens.SpSize10
import com.hazrat.islam24.presentation.Dimens.SpSize20
import com.hazrat.islam24.ui.theme.Islam24Theme

@Composable
fun LocationName(locationDetailsEntity: LocationDetailsEntity) {

    Location(modifier = Modifier,locationDetailsEntity)
}


@Composable
fun Location(modifier: Modifier = Modifier,
    locationDetailsEntity: LocationDetailsEntity) {
    Text(text = locationDetailsEntity.city?: locationDetailsEntity.village?:"",
        modifier = modifier,
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = SpSize20,
            color = Color.White
        )
    )
}

@Preview
@Composable
fun LocationNamePreview() {
    Islam24Theme {
//        LocationName()
    }
}