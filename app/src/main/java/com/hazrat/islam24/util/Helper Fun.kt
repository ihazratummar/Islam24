package com.hazrat.islam24.util

import android.os.VibrationEffect
import android.os.Vibrator

fun vibrate(vibrator: Vibrator) {
    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
}
