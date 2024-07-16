package com.hazrat.islam24.core.domain.model.athkar

data class TabItem(
    val title: String
)

val tabItems = listOf(
    com.hazrat.islam24.core.domain.model.athkar.TabItem(
        title = "Morning"
    ),
    com.hazrat.islam24.core.domain.model.athkar.TabItem(
        title = "Evening"
    )
)