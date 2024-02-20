package com.hazrat.islam24.presentation.navigator.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.R
import com.hazrat.islam24.presentation.Dimens.IconSize
import com.hazrat.islam24.presentation.Dimens.Size20
import com.hazrat.islam24.ui.theme.Islam24Theme

@Composable
fun AppBottomNavigation(
    items: List<BottomNavigationItem>,
    selectedItem: Int,
    onItemClick: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 10.dp
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = index == selectedItem
            NavigationBarItem(
                selected = index == selectedItem,
                onClick = { onItemClick(index) },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(Size20 *1.5f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        if (isSelected){
                            Text(text = item.text, style = MaterialTheme.typography.labelSmall)
                        }else {

                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = colorResource(id = R.color.white),
                    unselectedTextColor = colorResource(id = R.color.white),
                    indicatorColor = MaterialTheme.colorScheme.background
                ),
            )
        }
    }
}

data class BottomNavigationItem(
    @DrawableRes val icon: Int,
    val text: String
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NewsBottomNavigationPreview() {
    Islam24Theme(dynamicColor = false) {
        AppBottomNavigation(items = listOf(
            BottomNavigationItem(icon = R.drawable.naviconhome, text = "Home"),
            BottomNavigationItem(icon = R.drawable.naviconhome, text = "Prayer Time"),
            BottomNavigationItem(icon = R.drawable.naviconhome, text = "Quran"),
            BottomNavigationItem(icon = R.drawable.naviconhome, text = "Qibla"),
            BottomNavigationItem(icon = R.drawable.naviconhome, text = "Setting"),
        ), selectedItem = 0, onItemClick = {})
    }
}

