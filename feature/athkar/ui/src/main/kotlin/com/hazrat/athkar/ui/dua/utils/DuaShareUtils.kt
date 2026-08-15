package com.hazrat.athkar.ui.dua.utils

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

    private fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun shareToWhatsApp(context: Context, text: String) {
        val whatsappPkg = when {
            isPackageInstalled(context, "com.whatsapp") -> "com.whatsapp"
            isPackageInstalled(context, "com.whatsapp.w4b") -> "com.whatsapp.w4b"
            else -> null
        }

        if (whatsappPkg != null) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage(whatsappPkg)
                putExtra(Intent.EXTRA_TEXT, text)
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                shareText(context, text)
            }
        } else {
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
            shareText(context, text)
        }
    }

    fun shareToInstagram(context: Context, text: String) {
        if (isPackageInstalled(context, "com.instagram.android")) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.instagram.android")
                putExtra(Intent.EXTRA_TEXT, text)
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                shareText(context, text)
            }
        } else {
            Toast.makeText(context, "Instagram is not installed", Toast.LENGTH_SHORT).show()
            shareText(context, text)
        }
    }

    fun shareToFacebook(context: Context, text: String) {
        val fbPkg = when {
            isPackageInstalled(context, "com.facebook.katana") -> "com.facebook.katana"
            isPackageInstalled(context, "com.facebook.lite") -> "com.facebook.lite"
            else -> null
        }
        if (fbPkg != null) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage(fbPkg)
                putExtra(Intent.EXTRA_TEXT, text)
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                shareText(context, text)
            }
        } else {
            Toast.makeText(context, "Facebook is not installed", Toast.LENGTH_SHORT).show()
            shareText(context, text)
        }
    }

    fun shareToMessages(context: Context, text: String) {
        val sendIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:")
            putExtra("sms_body", text)
        }
        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(fallbackIntent, "Share Dua"))
        }
    }
}
