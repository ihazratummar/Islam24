package com.hazrat.islam24.core.presentation.qibla

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.common.LocationName
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.Hidaya
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.drawableToBitmap
import com.hazrat.islam24.util.vibrateDevice

/**
 * @author Hazrat Ummar Shaikh
 */


/**
 * QiblaScreen is a composable function that displays a compass indicating the Qibla direction.
 * It also vibrates the device when the user faces the Qibla.
 *
 * @param modifier The modifier to be applied to the QiblaScreen.
 * @param navController The NavHostController for navigation.
 * @param qiblaDirection The direction of the Qibla in degrees.
 * @param currentDirection The current direction of the device in degrees.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    qiblaDirection: Float, currentDirection: Float,
    viewModel: MainViewModel = hiltViewModel()
) {

    val locationName = viewModel.locationName.collectAsState().value

    val context = LocalContext.current
    val compassBgBitmap =
        remember { drawableToBitmap(context, R.drawable.compass3).asImageBitmap() }
    val qiblaIconBitmap =
        remember { drawableToBitmap(context, R.drawable.qiblaiconpoint).asImageBitmap() }
    val needleBitmap = remember { drawableToBitmap(context, R.drawable.needles).asImageBitmap() }
    val goldQaba = remember { drawableToBitmap(context, R.drawable.goldqaba).asImageBitmap() }

    val minTolerance = 3f // Adjusted tolerance range
    val maxTolerance = 3.5f // Adjusted tolerance range

    val directionDifference = qiblaDirection - currentDirection
    val normalizedDifference = (directionDifference + 360) % 360


    val isFacingQibla = (
            (normalizedDifference in 0.0..maxTolerance.toDouble()) ||
                    (normalizedDifference >= 360 - minTolerance && normalizedDifference <= 360)
            )

    var hasVibrated by remember { mutableStateOf(false) }

    // Vibrate when facing Qibla and not already vibrated
    if (isFacingQibla && !hasVibrated) {
        vibrateDevice(context, 200)
        hasVibrated = true // Set to true to prevent continuous vibration
    } else if (!isFacingQibla) {
        hasVibrated = false // Reset when not facing Qibla
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(id = R.string.qibla),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {

                        Icon(
                            painter = painterResource(id = R.drawable.backicon),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(MaterialTheme.dimens.size30)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size10))
            if (locationName.isNotEmpty()) {
                LocationName(locationName.first())
            }


            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Qibla: ${qiblaDirection.toInt()}°",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Hidaya
                )
                if (isFacingQibla) {
                    Icon(
                        modifier = Modifier.size(MaterialTheme.dimens.size60),
                        painter = painterResource(id = R.drawable.goldqaba),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(MaterialTheme.dimens.size30),
                        painter = painterResource(id = R.drawable.arrowup),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size40))
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .padding(MaterialTheme.dimens.size10)
                ) {
                    // Canvas for compass
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val compassCenter = Offset(size.width / 2, size.height / 2)
                        val compassRadius = size.minDimension / 2.5f

                        // Rotate the entire compass background image
                        rotate(degrees = -currentDirection, pivot = compassCenter) {
                            drawImage(
                                image = compassBgBitmap,
                                topLeft = Offset(
                                    compassCenter.x - compassBgBitmap.width / 2,
                                    compassCenter.y - compassBgBitmap.height / 2
                                )
                            )
                        }
                        // Draw the Qibla direction needle
                        rotate(degrees = qiblaDirection - currentDirection, pivot = compassCenter) {
                            val needleStartY = compassCenter.y - needleBitmap.height / 1.1f
                            drawImage(
                                image = needleBitmap,
                                topLeft = Offset(
                                    compassCenter.x - needleBitmap.width / 2f,
                                    needleStartY
                                )
                            )

                            // Draw the Qibla icon
                            if (!isFacingQibla) {
                                drawImage(
                                    image = qiblaIconBitmap,
                                    topLeft = Offset(
                                        compassCenter.x - qiblaIconBitmap.width / 2,
                                        compassCenter.y - compassRadius - qiblaIconBitmap.height / 1.1f
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}