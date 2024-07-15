package com.hazrat.islam24

import android.app.Application
import com.hazrat.islam24.util.Constants.ONESIGNAL_APP_ID
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Islam24Application.kt
@HiltAndroidApp
class Islam24Application: Application()