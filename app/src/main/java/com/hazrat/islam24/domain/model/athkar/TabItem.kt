package com.hazrat.islam24.domain.model.athkar

data class TabItem(
    val title: String
)

val tabItems = listOf(
    TabItem(
        title = "Morning"
    ),
    TabItem(
        title = "Evening"
    )
)