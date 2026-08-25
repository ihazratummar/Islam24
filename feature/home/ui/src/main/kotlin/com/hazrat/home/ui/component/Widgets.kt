package com.hazrat.home.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import kotlinx.serialization.Serializable

@Composable
fun QuickAccessMenu(
    onClick: (HomePageNavIcons) -> Unit
) {
    val navIcons = remember {
        listOf(
            HomePageNavIcons.AsmaUlHusna,
            HomePageNavIcons.Calendar,
            HomePageNavIcons.Athkar,
            HomePageNavIcons.Dua,
            HomePageNavIcons.Tasbih,
            HomePageNavIcons.Zakat,
        )
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.spacedBy(dimens.space8),
        maxItemsInEachRow = 3
    ) {

        navIcons.forEach {
            HomeWidgetsIcons(
                iconData = it,
                onClick = { onClick(it) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeWidgetsIcons(
    iconData: HomePageNavIcons,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = dimens.space12, horizontal = dimens.space12),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(dimens.space64)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(dimens.cornerMd)
                )
                .clickable(
                    onClick = onClick
                )
            ,
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconData.icons),
                contentDescription = iconData.name.toString(),
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(dimens.iconLg)
                    .padding(dimens.space2)
            )
        }
        Text(
            text = stringResource(iconData.name),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = dimens.space8)
        )
    }
}

sealed class HomePageNavIcons(
    val icons: Int,
    val name: Int,
    val route: HomeRoutes
) {
    data object AsmaUlHusna :
        HomePageNavIcons(
            icons = R.drawable.allah,
            name = R.string.nav_asmaul_husna,
            route = HomeRoutes.NamesOfAllah
        )

    data object Calendar :
        HomePageNavIcons(
            icons = R.drawable.calendar,
            name = R.string.nav_calendar,
            route = HomeRoutes.Calendar
        )

    data object Athkar :
        HomePageNavIcons(
            icons = R.drawable.zikir,
            name = R.string.nav_zikir,
            route = HomeRoutes.Athkar
        )

    data object Dua :
        HomePageNavIcons(
            icons = R.drawable.dua,
            name = R.string.nav_dua,
            route = HomeRoutes.DuaRoute
        )

    data object Qibla :
        HomePageNavIcons(
            icons = R.drawable.kaba,
            name = R.string.nav_qibla,
            route = HomeRoutes.Qibla
        )

    data object Zakat :
        HomePageNavIcons(
            icons = R.drawable.zakat,
            name = R.string.nav_zakat,
            route = HomeRoutes.Zakat
        )

    data object Tasbih : HomePageNavIcons(
        icons = R.drawable.tasbih,
        name = R.string.nav_tasbih,
        route = HomeRoutes.TasbihRoute
    )
}

@Serializable
sealed class HomeRoutes {
    @Serializable
    data object NamesOfAllah : HomeRoutes()

    @Serializable
    data object Calendar : HomeRoutes()

    @Serializable
    data object Athkar : HomeRoutes()

    @Serializable
    data object TasbihRoute : HomeRoutes()

    @Serializable
    data object DuaRoute : HomeRoutes()

    @Serializable
    data object HisnulMuslimGridRoute : HomeRoutes()

    @Serializable
    data class DuaCategoryDetailRoute(val categoryId: String) : HomeRoutes()

    @Serializable
    data object DuaBookmarksRoute : HomeRoutes()

    @Serializable
    data object DuaRecentsRoute : HomeRoutes()

    @Serializable
    data class DuaItemRoute(val categoryId: Int) : HomeRoutes()

    @Serializable
    data object Qibla : HomeRoutes()

    @Serializable
    data object Zakat : HomeRoutes()

    @Serializable
    data object HomeScreenWidgetsRoute : HomeRoutes()
}
