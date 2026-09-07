package com.hazrat.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

/**
 * Universal Image Sharing Helper for Islam 24.
 * Generates and shares high-resolution visual cards to social platforms and system sheet.
 * @author hazratummar
 */
object ImageShareUtils {

    fun saveBitmapToCache(
        context: Context,
        bitmap: Bitmap,
        filename: String = "islam24_share_${System.currentTimeMillis()}.png"
    ): Uri {
        val cacheDir = File(context.cacheDir, "shared_images")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        val file = File(cacheDir, filename)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun shareImage(context: Context, imageUri: Uri, caption: String = "") {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            if (caption.isNotBlank()) {
                putExtra(Intent.EXTRA_TEXT, caption)
            }
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, "Share Card").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    fun shareToWhatsApp(context: Context, imageUri: Uri, caption: String = "") {
        val targets = listOf("com.whatsapp", "com.whatsapp.w4b")
        for (pkg in targets) {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    setPackage(pkg)
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    if (caption.isNotBlank()) {
                        putExtra(Intent.EXTRA_TEXT, caption)
                    }
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                return
            } catch (_: ActivityNotFoundException) {
            } catch (_: Exception) {
            }
        }
        shareImage(context, imageUri, caption)
    }

    fun shareToInstagram(context: Context, imageUri: Uri) {
        try {
            val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
                setDataAndType(imageUri, "image/png")
                putExtra("interactive_asset_uri", imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            try {
                val feedIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    setPackage("com.instagram.android")
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(feedIntent)
            } catch (_: Exception) {
                shareImage(context, imageUri)
            }
        } catch (_: Exception) {
            shareImage(context, imageUri)
        }
    }

    fun shareToFacebookStory(context: Context, imageUri: Uri) {
        try {
            val intent = Intent("com.facebook.stories.ADD_TO_STORY").apply {
                setDataAndType(imageUri, "image/png")
                putExtra("interactive_asset_uri", imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            shareImage(context, imageUri)
        } catch (_: Exception) {
            shareImage(context, imageUri)
        }
    }

    fun shareToMessages(context: Context, imageUri: Uri, caption: String = "") {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                if (caption.isNotBlank()) {
                    putExtra(Intent.EXTRA_TEXT, caption)
                }
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val chooser = Intent.createChooser(intent, "Send via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (_: Exception) {
            shareImage(context, imageUri, caption)
        }
    }
}
