package com.hazrat.zakat.screen.zakat.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hazrat.common.BasicTopBar
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import com.hazrat.zakat.screen.zakat.ZakatEvent
import com.hazrat.zakat.screen.zakat.ZakatState
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NisabScreen(
    modifier: Modifier = Modifier,
    zakatState: ZakatState,
    zakatEvent: (ZakatEvent) -> Unit,
    onSubmit: () -> Unit,
    onBackClick: () -> Unit
) {

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.space48))
            BasicTopBar(
                onBackClick = { onBackClick.invoke() }
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(dimens.space12),
            ) {
                Text(text = stringResource(R.string.nisab_is_a_threshold_referring_to_the_minimum_amount_of_wealth))
                val nisabAmount = 11.66 * 52.50
                val roundAmount = String.format(Locale.getDefault(), "%.2f", nisabAmount)
                Spacer(modifier = Modifier.height(dimens.space12))
                Text(
                    text = stringResource(
                        R.string.the_nisab_threshold_for_silver_is_gm_52_50_tola_vori_or_the_cash_equivalent,
                        roundAmount
                    )
                )
                Spacer(modifier = Modifier.height(dimens.space12))
                Text(text = stringResource(R.string.to_calculate_nisab_enter_the_current_value_of_silver_gm))
                Spacer(modifier = Modifier.height(dimens.space20))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = zakatState.silverPrice,
                    onValueChange = { zakatEvent(ZakatEvent.OnPriceChange(it)) },
                    placeholder = {
                        Text(
                            text = "Write the silver price here.",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            zakatEvent(ZakatEvent.SubmitNisab(zakatState.silverPrice))
                            onSubmit()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(dimens.space12))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        zakatEvent(ZakatEvent.SubmitNisab(zakatState.silverPrice))
                        onSubmit()
                    },
                    enabled = zakatState.silverPrice != "" && zakatState.isSilverPriceValid,
                    shape = RoundedCornerShape(dimens.cornerMd)
                ) {
                    Text(text = "Submit")
                }
            }
        }
        }
        }