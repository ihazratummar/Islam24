package com.hazrat.islam24.presentation.namesofallah

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.domain.model.namesofallah.Data
import com.hazrat.islam24.domain.model.namesofallah.En
import com.hazrat.islam24.presentation.Dimens.Size1
import com.hazrat.islam24.presentation.Dimens.Size10
import com.hazrat.islam24.presentation.Dimens.Size15
import com.hazrat.islam24.presentation.Dimens.Size2
import com.hazrat.islam24.presentation.Dimens.Size20
import com.hazrat.islam24.presentation.Dimens.Size3
import com.hazrat.islam24.presentation.Dimens.Size35
import com.hazrat.islam24.presentation.Dimens.Size5
import com.hazrat.islam24.presentation.Dimens.SpSize15
import com.hazrat.islam24.presentation.Dimens.SpSize25

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamesOfAllahScreen(viewModel: NamesViewModel = hiltViewModel(), navController: NavController) {

    val names = viewModel.names.observeAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(modifier = Modifier
//        .statusBarsPadding()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = Color(0xFFFDD017)
            ),
                title = { Text(text = "99 Names Of Allah", color = Color.White) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()

                            }
                            .padding(horizontal = Size10)
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(names.value ?: emptyList()) { name ->
                NameCard(name = name)
            }
        }
    }

}


@Composable
fun NameCard(name: Data) {

    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xff011403),
                        Color(0xff011403),
                    )
                )
            )
            .clickable {
                expanded = !expanded
            }
            .padding(horizontal = Size15, vertical = Size1),
        shape = RoundedCornerShape(Size10),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Column() {
            Row(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(start = Size5, top = Size20),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${name.number}.", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = SpSize15,
                            color = Color(0xffFDD017)
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(Size5)
                ) {
                    Text(
                        text = name.transliteration, style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = SpSize25,
                            color = Color(0xFFFFFFFF)
                        )
                    )
                    Text(
                        text = name.en.meaning, style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = SpSize15,
                            color = Color(0xC8FDD017)
                        )
                    )
                    Spacer(modifier = Modifier.height(Size3))
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow"
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(Size5),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = SpSize25,
                            color = Color(0xffFDD017)
                        )
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Divider(thickness =Size2, color = Color(0xffFDD017))
                Column(
                    modifier = Modifier
                        .padding(start = Size35, top = Size20, end = Size10)
                ) {
                    Text(text = "Ayath: ${name.found}", color = Color.White)
                    Spacer(modifier = Modifier.height(Size5))
                    Text(text = name.en.desc, color = Color.White)
                }
            }
        }
    }
}

@Preview
@Composable
fun NameCardPreview() {
    val sampleName = Data(
        en = En(
            desc = "Description of Al-Rahman",
            meaning = "Meaning of Al-Rahman"
        ),
        name = "الرَّحْمَنُ",
        found = "Yes",
        number = 1,
        transliteration = "Ar-Rahman"
    )
    NameCard(name = sampleName)
}