package com.hazrat.athkar.ui.dua.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.hazrat.model.DuaItemModel

/**
 * Utility functions for sharing Duas directly to WhatsApp, Stories, Messages, and System Chooser.
 * @author hazratummar
 */
object DuaShareUtils {

    fun buildShareText(dua: DuaItemModel, chapterTitle: String): String {
        return buildString {
            if (chapterTitle.isNotBlank()) {
                append("• ")
                append(chapterTitle)
                append(" •\n\n")
            }
            append(dua.arabicText.trim())
            append("\n\n")
            if (dua.transliteration.isNotBlank()) {
                append(dua.transliteration.trim())
                append("\n\n")
            }
            append(dua.translation.trim())
            if (dua.reference.isNotBlank()) {
                append("\n\nReference: ")
                append(dua.reference.trim())
            }
            append("\n\n— Shared via Islam 24")
        }
    }

    fun shareToWhatsApp(context: Context, text: String) {
        val targets = listOf("com.whatsapp", "com.whatsapp.w4b")
        for (pkg in targets) {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    setPackage(pkg)
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                context.startActivity(intent)
                return
            } catch (_: ActivityNotFoundException) {
                // Try next package
            } catch (_: Exception) {
            }
        }
        // Fallback to system share chooser if direct WhatsApp package wasn't reached
        shareText(context, text)
    }

    fun shareToInstagram(context: Context, text: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.instagram.android")
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            shareText(context, text)
        } catch (_: Exception) {
            shareText(context, text)
        }
    }

    fun shareToFacebook(context: Context, text: String) {
        val fbTargets = listOf("com.facebook.katana", "com.facebook.lite", "com.facebook.orca")
        for (pkg in fbTargets) {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    setPackage(pkg)
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                context.startActivity(intent)
                return
            } catch (_: ActivityNotFoundException) {
                // Try next
            } catch (_: Exception) {
            }
        }
        shareText(context, text)
    }

    fun shareToMessages(context: Context, text: String) {
        try {
            val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:")
                putExtra("sms_body", text)
            }
            context.startActivity(sendIntent)
        } catch (_: Exception) {
            shareText(context, text, "com.google.android.apps.messaging")
        }
    }

    fun shareText(context: Context, text: String, targetPackage: String? = null) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            if (!targetPackage.isNullOrBlank()) {
                setPackage(targetPackage)
            }
        }
        try {
            if (targetPackage != null) {
                context.startActivity(sendIntent)
            } else {
                context.startActivity(Intent.createChooser(sendIntent, "Share Dua"))
            }
        } catch (_: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            try {
                context.startActivity(Intent.createChooser(fallbackIntent, "Share Dua"))
            } catch (_: Exception) {
                Toast.makeText(context, "No app available to share", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
