package com.hazrat.model

data class ReleaseNote(
    val versionCode: Int,
    val versionName: String,
    val changes: List<String>
)
