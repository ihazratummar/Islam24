package com.hazrat.islam24.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.hazrat.islam24.util.Constants.INTERNALSTORAGEPICTUREFOLDER
import com.hazrat.islam24.util.Constants.PROFILE_PICTURE
import java.io.File
import java.util.Locale

fun vibrate(vibrator: Vibrator) {
    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
}




fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
    val drawable: Drawable = ContextCompat.getDrawable(context, drawableId)!!
    return drawable.toBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
}

fun vibrateDevice(context: Context, vibrateTime: Long) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    if (vibrator != null && vibrator.hasVibrator()) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                vibrateTime, // Vibration duration in milliseconds
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }
}

fun NavController.popUpTo(destination: String) = navigate(destination) {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }
    restoreState = true
}

fun Context.getActivity(): Activity? = when(this){
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


@Composable
fun getSystemLanguage(): String {
    val context = LocalContext.current
    val locale: Locale = context.resources.configuration.locales[0]
    return locale.language
}


fun checkSystemLanguage(context: Context): String {
    val locale: Locale =
        context.resources.configuration.locales[0]
    return locale.language
}

fun getCacheProfilePicture(context: Context): File? {
    val directory = File(context.filesDir, INTERNALSTORAGEPICTUREFOLDER)
    val file = File(directory, "$PROFILE_PICTURE.jpg") // Look for the file in the specified directory
    return if (file.exists()) file else null
}

fun File.toUri(): Uri {
    return Uri.fromFile(this)
}

fun Int.toArabicNumerals(): String {
    val arabicDigits = listOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    return this.toString().map { arabicDigits[it.digitToInt()] }.joinToString("")
}