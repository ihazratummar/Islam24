package com.hazrat.islam24.auth.presentation.profileScreen.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileEvent
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

fun Modifier.profileCardShimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondaryContainer,
            ),
            start = Offset(startOffsetX, 0F),
            end = Offset(
                startOffsetX + size.width.toFloat(), size.height.toFloat()
            )
        ),
        shape = RoundedCornerShape(dimens.size10)
    ).onGloballyPositioned {
        size = it.size
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RatingBottomSheet(
    profileEvent: (ProfileEvent) -> Unit,
    hapticFeedback: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            profileEvent(ProfileEvent.OpenRatingDialog)
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size10),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var rating by remember {
                mutableIntStateOf(0)
            }
            Text(
                text = "Rate us",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    Icon(
                        painter = if (rating > index) painterResource(id = R.drawable.star) else painterResource(
                            R.drawable.outlinstar
                        ),
                        contentDescription = "Star",
                        modifier = Modifier
                            .size(dimens.size60)
                            .clickable {
                                rating = index + 1
                            }
                            .padding(dimens.size5),
                        tint = if (index < rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimens.size10))
            if (rating in 1..3) {
                Text(
                    text = stringResource(R.string.thank_you_for_your_feedback_we_appreciate_your_input),
                    modifier = Modifier.padding(dimens.size5),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (rating >= 4) {
                Text(
                    text = stringResource(R.string.thank_you_for_your_positive_feedback_we_d_love_for_you_to_rate_us),
                    modifier = Modifier.padding(dimens.size5),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = {
                        profileEvent(ProfileEvent.GoToRate)
                        hapticFeedback()
                    },
                    modifier = Modifier
                        .padding(dimens.size10)
                        .fillMaxWidth()
                        .height(dimens.size60),
                    shape = RoundedCornerShape(dimens.size10),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Go to Rate",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}