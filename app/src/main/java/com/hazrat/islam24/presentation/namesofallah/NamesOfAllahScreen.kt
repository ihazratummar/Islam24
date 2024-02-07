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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.domain.model.namesofallah.Data
import com.hazrat.islam24.domain.model.namesofallah.En

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
                title = { Text(text = "99 Names Of Allah") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()

                            }
                            .padding(horizontal = 10.dp)
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
            .padding(horizontal = 15.dp, vertical = 1.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Column() {
            Row(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(start = 5.dp, top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${name.number}.", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xffFDD017)
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(5.dp)
                ) {
                    Text(
                        text = name.transliteration, style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = Color(0xFFFFFFFF)
                        )
                    )
                    Text(
                        text = name.en.meaning, style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xC8FDD017)
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow"
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = Color(0xffFDD017)
                        )
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Divider(thickness = 2.dp, color = Color(0xffFDD017))
                Column(
                    modifier = Modifier
                        .padding(start = 35.dp, top = 20.dp, end = 10.dp)
                ) {
                    Text(text = "Ayath: ${name.found}", color = Color.White)
                    Spacer(modifier = Modifier.height(5.dp))
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