package com.hazrat.islam24.core.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 22-01-2025
 */

@Composable
fun HomeScreenTopBoxLoading() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimens.size5)
            .height(dimens.size250),
        shape = RoundedCornerShape(dimens.size30),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.size20),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .clip(shape = RoundedCornerShape(dimens.size10))
                        .height(dimens.size60)
                        .shimmerEffect()

                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clip(shape = RoundedCornerShape(dimens.size10))
                        .height(dimens.size40)
                        .shimmerEffect()

                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clip(shape = RoundedCornerShape(dimens.size10))
                        .height(dimens.size30)
                        .shimmerEffect()

                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .clip(shape = RoundedCornerShape(dimens.size10))
                        .height(dimens.size40)
                        .shimmerEffect()

                )
            }
        }
    }
}