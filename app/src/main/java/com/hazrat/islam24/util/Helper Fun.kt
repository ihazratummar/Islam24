package com.hazrat.islam24.util

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun vibrate(vibrator: Vibrator) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(100)
    }
}
