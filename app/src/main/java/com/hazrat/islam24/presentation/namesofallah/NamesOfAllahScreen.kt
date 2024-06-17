package com.hazrat.islam24.presentation.namesofallah

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.data.entity.NameEntity
import com.hazrat.islam24.presentation.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamesOfAllahScreen(viewModel: MainViewModel = hiltViewModel(), navController: NavController) {

    val names = viewModel.names.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(modifier = Modifier
//        .statusBarsPadding()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                title =
                {
                    Text(
                        text = "Names Of Allah",
                        color = colorResource(id = R.color.text),
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()

                            },
                        tint = colorResource(id = R.color.primary)
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(names.value) { name ->
                NameCard(name = name)
            }
        }
    }
}


@Composable
fun NameCard(name: NameEntity) {

    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                expanded = !expanded
            }
            .padding(
                horizontal = MaterialTheme.dimens.size15,
                vertical = MaterialTheme.dimens.size1
            ),
        shape = RoundedCornerShape(MaterialTheme.dimens.size10),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                        .padding(
                            start = MaterialTheme.dimens.size5,
                            top = MaterialTheme.dimens.size20
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${name.number}.", style = MaterialTheme.typography.displaySmall,
                        color = colorResource(id = R.color.text)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(MaterialTheme.dimens.size5)
                ) {
                    Text(
                        text = name.transliteration, style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = colorResource(id = R.color.text)
                        )
                    )
                    Text(
                        text = name.meaning, style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = colorResource(id = R.color.text)
                        )
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size3))
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow",
                        tint = colorResource(id = R.color.primary)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.8f)
                        .padding(MaterialTheme.dimens.size5),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name.name,
                        style = MaterialTheme.typography.displayMedium,
                        color = colorResource(id = R.color.primary)
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                HorizontalDivider(
                    thickness = MaterialTheme.dimens.size2,
                    color = colorResource(id = R.color.text)
                )
                Column(
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.dimens.size35,
                            top = MaterialTheme.dimens.size20, end = MaterialTheme.dimens.size10
                        )
                ) {
                    Text(text = "Ayat: ${name.found}", color = colorResource(id = R.color.text))
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size5))
                    Text(text = name.enDec, color = colorResource(id = R.color.text))
                }
            }
        }
    }
}
