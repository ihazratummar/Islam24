package com.hazrat.islam24.core.presentation.tasbih

import android.content.Context
import android.os.Vibrator
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.TasbihCounterEntity
import com.hazrat.islam24.core.presentation.tasbih.tasbihcomponent.RepeatCountDialog
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.AlQalam
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.vibrate

@Composable
fun TasbihCounterApp(viewModel: MainViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val myTasbihCounter by viewModel.tasbihCounter.collectAsState(initial = emptyList())
    val tasbih = myTasbihCounter.firstOrNull()


//    val selectedPhrase by remember { mutableStateOf(tasbihPhraseList[0]) }
    var repeatCount by remember { mutableIntStateOf(33) } // Start from 33 initially
    var tasbihCount by remember { mutableIntStateOf(tasbih?.tasbihCount ?: 1) }
    var totalCount by remember { mutableIntStateOf(tasbih?.totalCount ?: 1) }
    var roundCount by remember { mutableIntStateOf(0) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }


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
                            color = colorResource(id = R.color.text)
                        ),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.tasbihTarget, repeatCount), modifier = Modifier,
                                color = colorResource(id = R.color.text)
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
                            color = colorResource(id = R.color.text)
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
                                color = colorResource(id = R.color.text)
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
                                vibrate(vibrator!!)
                            },
                            modifier = Modifier
                                .size(MaterialTheme.dimens.size300)
                                .padding(MaterialTheme.dimens.size15),
                            shape = CircleShape,
                            border = BorderStroke(
                                MaterialTheme.dimens.size2,
                                colorResource(id = R.color.text)
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
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = colorResource(id = R.color.text)
                                )
                                if (tasbih?.tasbihCount == 0) Text(
                                    stringResource(R.string.click_to_start), color = colorResource(id = R.color.text),
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
                                color = colorResource(id = R.color.text)
                            ),
                            shape = RoundedCornerShape(MaterialTheme.dimens.size20)
                        ) {
                            Text(
                                stringResource(R.string.reset), color = colorResource(id = R.color.primary),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
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
                color = colorResource(id = R.color.text)
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
                    color = colorResource(id = R.color.text),
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.size5)
                )
            }
        }
    }
}

@Composable
private fun TasbihHeader(
    phrase: com.hazrat.islam24.core.domain.model.TasbihPhrase,
    viewModel: MainViewModel
) {
    val selectedPhrase = viewModel.selectedPhrase
    Card(
        modifier = Modifier
            .padding(MaterialTheme.dimens.size8)
            .width(MaterialTheme.dimens.size300)
            .height(MaterialTheme.dimens.size200)
            .clickable {
                viewModel.selectedPhrase = phrase
                viewModel.resetTasbihCount()
            },
        elevation = CardDefaults.cardElevation(MaterialTheme.dimens.size4),
        colors = CardDefaults.cardColors(
            containerColor = if (phrase == selectedPhrase) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.secondaryContainer,
            contentColor = if (phrase == selectedPhrase) MaterialTheme.colorScheme.onPrimaryContainer
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
                color = colorResource(id = R.color.text),
                textAlign = TextAlign.Center,
                fontFamily = AlQalam
            )
            Text(
                phrase.enText, style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.text)

            )
        }
    }
}


@Preview()
@Composable
fun PreviewTasbihCounterApp() {
    Islam24Theme {
        val tasbihCounterEntity = TasbihCounterEntity(totalCount = 10)
        ToTalCount(tasbihCounterEntity)
    }
}