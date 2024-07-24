package com.hazrat.islam24.core.presentation.tasbih

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.TasbihCounterEntity
import com.hazrat.islam24.core.domain.model.TasbihPhrase
import com.hazrat.islam24.core.presentation.tasbih.tasbihcomponent.RepeatCountDialog
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.AlQalam
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.vibrateDevice

@Composable
fun TasbihCounterApp(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val myTasbihCounter by viewModel.tasbihCounter.collectAsState(initial = emptyList())
    val tasbih = myTasbihCounter.firstOrNull()


//    val selectedPhrase by remember { mutableStateOf(tasbihPhraseList[0]) }
    var repeatCount by remember { mutableIntStateOf(33) } // Start from 33 initially
    var tasbihCount by remember { mutableIntStateOf(tasbih?.tasbihCount ?: 1) }
    var totalCount by remember { mutableIntStateOf(tasbih?.totalCount ?: 1) }
    var roundCount by remember { mutableIntStateOf(0) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val vibrator by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Phrase Selection
        LazyRow {
            items(com.hazrat.islam24.core.domain.model.tasbihPhraseList) { phrase ->
                TasbihHeader(phrase, viewModel)
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size4))
        // Tasbih Counter
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size10))
            }
            item {
                ToTalCount(tasbih)
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Repeat Count Selection
                    Card(
                        modifier = Modifier
                            .clickable { isDialogOpen = true }
                            .width(MaterialTheme.dimens.size150)
                            .height(MaterialTheme.dimens.size50)
                            .padding(MaterialTheme.dimens.size5),
                        colors = CardDefaults.cardColors(Color.Transparent),
                        border = BorderStroke(
                            MaterialTheme.dimens.size1,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.tasbihTarget, repeatCount),
                                modifier = Modifier,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // Round Counter
                    Card(
                        modifier = Modifier
                            .width(MaterialTheme.dimens.size150)
                            .height(MaterialTheme.dimens.size50)
                            .padding(MaterialTheme.dimens.size5),
                        colors = CardDefaults.cardColors(Color.Transparent),
                        border = BorderStroke(
                            MaterialTheme.dimens.size1,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(R.string.round, roundCount),
                                modifier = Modifier
                                    .padding(MaterialTheme.dimens.size5),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                if (tasbih != null) {
                                    viewModel.insertTasbih(
                                        TasbihCounterEntity(
                                            totalCount = tasbih.totalCount + 1,
                                            tasbihCount = tasbih.tasbihCount + 1
                                        )
                                    )
                                } else {
                                    viewModel.insertTasbih(
                                        TasbihCounterEntity(
                                            totalCount = totalCount,
                                            tasbihCount = tasbihCount
                                        )
                                    )
                                }

                                totalCount++
                                tasbihCount++
                                if (tasbihCount % repeatCount == 0) {
                                    roundCount++
                                    viewModel.resetTasbihCount()
                                }
                                vibrateDevice(context, 100)
                            },
                            modifier = Modifier
                                .size(MaterialTheme.dimens.size300)
                                .padding(MaterialTheme.dimens.size15),
                            shape = CircleShape,
                            border = BorderStroke(
                                MaterialTheme.dimens.size2,
                                MaterialTheme.colorScheme.onBackground
                            ),
                            colors = ButtonDefaults.buttonColors(Color.Transparent)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val countText = "${tasbih?.tasbihCount ?: "0"} / $repeatCount"
                                Text(
                                    text = countText,
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                if (tasbih?.tasbihCount == 0) Text(
                                    stringResource(R.string.click_to_start),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }

                        }
                        // Reset Button
                        Button(
                            onClick = {
                                viewModel.resetTasbihCount()
                                roundCount = 0
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(horizontal = MaterialTheme.dimens.size10),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            border = BorderStroke(
                                MaterialTheme.dimens.size1,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            shape = RoundedCornerShape(MaterialTheme.dimens.size20)
                        ) {
                            Text(
                                stringResource(R.string.reset),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                }
            }
        }
        // Dialog for Repeat Count Selection
        if (isDialogOpen) {
            RepeatCountDialog(
                onDismiss = { isDialogOpen = false },
                onRepeatCountSelected = { selectedCount ->
                    repeatCount = selectedCount
                    isDialogOpen = false
                }
            )
        }
    }

}

@Composable
private fun ToTalCount(tasbih: TasbihCounterEntity?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier
                .width(MaterialTheme.dimens.size200)
                .height(MaterialTheme.dimens.size50)
                .padding(MaterialTheme.dimens.size5),
            colors = CardDefaults.cardColors(Color.Transparent),
            border = BorderStroke(
                MaterialTheme.dimens.size1,
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val countText = tasbih?.totalCount ?: "0"
                Text(
                    stringResource(R.string.total_count, countText),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.size5)
                )
            }
        }
    }
}

@Composable
private fun TasbihHeader(
    phrase: TasbihPhrase,
    viewModel: MainViewModel
) {
    val selectedPhrase = viewModel.selectedPhrase
    Card(
        modifier = Modifier
            .padding(MaterialTheme.dimens.size8)
            .width(MaterialTheme.dimens.size300)
            .height(MaterialTheme.dimens.size200)
            .clickable(
                onClick = {
                    viewModel.selectedPhrase = phrase
                    viewModel.resetTasbihCount()
                },
                onClickLabel = stringResource(id = R.string.tasbihnames)
            ),
        elevation = CardDefaults.cardElevation(MaterialTheme.dimens.size4),
        colors = CardDefaults.cardColors(
            containerColor = if (phrase == selectedPhrase) MaterialTheme.colorScheme.primaryContainer.copy(
                0.8f
            )
            else MaterialTheme.colorScheme.secondaryContainer,
            contentColor = if (phrase == selectedPhrase) MaterialTheme.colorScheme.onPrimaryContainer.copy(
                0.8f
            )
            else MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.dimens.size10),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                phrase.arText, style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontFamily = AlQalam
            )
            Text(
                phrase.enText, style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground

            )
        }
    }
}