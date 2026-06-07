package com.hazrat.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 09-01-2025
 */


class MuteReceiver : BroadcastReceiver(), KoinComponent {


    private val mediaPlayerHelper: MediaPlayerHelper by inject()

    override fun onReceive(context: Context, intent: Intent) {
        mediaPlayerHelper.stopAzan()
        context.stopService(Intent(context, AzanPlaybackService::class.java))
    }
}