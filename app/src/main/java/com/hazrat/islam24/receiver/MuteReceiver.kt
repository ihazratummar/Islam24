package com.hazrat.islam24.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hazrat.islam24.notification.MediaPlayerHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 09-01-2025
 */

@AndroidEntryPoint
class MuteReceiver : BroadcastReceiver() {

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    override fun onReceive(context: Context, intent: Intent) {
        mediaPlayerHelper.stopAzan()
    }
}