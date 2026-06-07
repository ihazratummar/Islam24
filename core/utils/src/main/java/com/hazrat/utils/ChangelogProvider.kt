package com.hazrat.utils

import android.content.Context
import com.hazrat.model.ReleaseNote
import java.io.BufferedReader
import java.io.InputStreamReader

object ChangelogProvider {

    /**
     * Parses the changelog.md from assets and returns a list of ReleaseNotes.
     * Industry-grade: Automates UI updates directly from your documentation.
     */
    fun getReleaseNotes(context: Context): List<ReleaseNote> {
        val releaseNotes = mutableListOf<ReleaseNote>()
        try {
            val inputStream = context.assets.open("changelog.md")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var currentVersion: String? = null
            var currentChanges = mutableListOf<String>()
            var versionCode = 0 // You might want to map this or extract it if added to MD

            reader.forEachLine { line ->
                when {
                    line.startsWith("## [") -> {
                        // If we were already tracking a version, save it before starting new one
                        if (currentVersion != null) {
                            releaseNotes.add(ReleaseNote(versionCode, currentVersion!!, currentChanges.toList()))
                        }
                        // Extract version e.g. "3.0.0" from "## [3.0.0] - 2024-05-22"
                        currentVersion = line.substringAfter("[").substringBefore("]")
                        currentChanges = mutableListOf()
                        versionCode-- // Dummy decrementing code or handle mapping
                    }
                    line.startsWith("- ") || line.startsWith("* ") -> {
                        currentChanges.add(line.substring(2))
                    }
                }
            }
            // Add the last one
            if (currentVersion != null) {
                releaseNotes.add(ReleaseNote(versionCode, currentVersion!!, currentChanges.toList()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return releaseNotes
    }

    fun getLatestVersionCode(context: Context): Int {
        // Since we are parsing version names, we can compare them or use the first entry
        // For simplicity, we'll return a hash of the latest version name as a code
        return getReleaseNotes(context).firstOrNull()?.versionName.hashCode()
    }
}
