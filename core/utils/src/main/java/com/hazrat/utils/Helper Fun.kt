package com.hazrat.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.hazrat.utils.Constants.INTERNALSTORAGEPICTUREFOLDER
import com.hazrat.utils.Constants.PROFILE_PICTURE
import java.io.File
import java.util.Locale

fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
    val drawable: Drawable = ContextCompat.getDrawable(context, drawableId)!!
    return drawable.toBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
}

//fun vibrateDevice(context: Context, vibrateTime: Long) {
//    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
//    if (vibrator != null && vibrator.hasVibrator()) {
//        vibrator.vibrate(
//            VibrationEffect.createOneShot(
//                vibrateTime, // Vibration duration in milliseconds
//                VibrationEffect.DEFAULT_AMPLITUDE
//            )
//        )
//    }
//}


fun openAppSettings(context: Context) {

    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${context.packageName}")
    )

    context.startActivity(intent)
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


@Composable
fun getSystemLanguage(): String {
    val locale: Locale = LocalConfiguration.current.locales[0]
    return locale.language
}


fun checkSystemLanguage(context: Context): String {
    val locale: Locale = context.resources.configuration.locales[0]
    return locale.language
}

fun getCacheProfilePicture(context: Context): File? {
    val directory = File(context.filesDir, INTERNALSTORAGEPICTUREFOLDER)
    val file =
        File(directory, "$PROFILE_PICTURE.jpg") // Look for the file in the specified directory
    return if (file.exists()) file else null
}

fun File.toUri(): Uri {
    return Uri.fromFile(this)
}







fun hapticFeedbacks(
    isEnable: Boolean,
    hapticFeedback: HapticFeedback
) {
    if (isEnable) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }
}



fun isMp3FileValid(filePath: String): Boolean {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(filePath)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toIntOrNull() ?: 0
        retriever.release()
        duration > 0  // MP3 is valid if it has a non-zero duration
    } catch (e: Exception) {
        Log.e("FileCheck", "Error checking MP3 file: ${e.message}", e)
        false
    }
}
