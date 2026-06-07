package com.hazrat.utils

import android.content.Context
import com.hazrat.model.ReleaseNote
import java.io.BufferedReader
import java.io.InputStreamReader

class ChangelogRepository(private val context: Context) {

    fun getReleaseNotes(): List<ReleaseNote> {
        val releaseNotes = mutableListOf<ReleaseNote>()
        try {
            val inputStream = context.assets.open("changelog.md")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var currentVersion: String? = null
            var currentChanges = mutableListOf<String>()
            var versionCode = 0

            reader.forEachLine { line ->
                when {
                    line.startsWith("## [") -> {
                        if (currentVersion != null) {
                            releaseNotes.add(ReleaseNote(versionCode, currentVersion!!, currentChanges.toList()))
                        }
                        currentVersion = line.substringAfter("[").substringBefore("]")
                        currentChanges = mutableListOf()
                        versionCode--
                    }
                    line.startsWith("- ") || line.startsWith("* ") -> {
                        currentChanges.add(line.substring(2))
                    }
                }
            }
            if (currentVersion != null) {
                releaseNotes.add(ReleaseNote(versionCode, currentVersion!!, currentChanges.toList()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return releaseNotes
    }

    fun getLatestVersionCode(): Int {
        return getReleaseNotes().firstOrNull()?.versionName.hashCode()
    }
}
