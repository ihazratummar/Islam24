package com.hazrat.ui.animations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-06-2025
 */

@Composable
fun LottieAnimation(
    modifier: Modifier = Modifier,
    lottieJson: Int
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieJson))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )

}