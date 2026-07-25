package com.hazrat.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.hazrat.ui.R
import com.hazrat.utils.formatCurrency
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * BroadcastReceiver triggered by AlarmManager when a Zakat Hawl reminder is due.
 * Works even when the app is completely closed or killed in background.
 *
 * @author Hazrat Ummar Shaikh
 */
class ZakatNotificationReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationManager: NotificationManagerCompat by inject()

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent?) {
        val zakatId = intent?.getStringExtra(ZakatAlarmScheduler.EXTRA_ZAKAT_ID) ?: return
        val zakatAmount = intent.getDoubleExtra(ZakatAlarmScheduler.EXTRA_ZAKAT_AMOUNT, 0.0)

        Log.d("ZakatNotificationReceiver", "Alarm triggered for Zakat ID: $zakatId (Amount: $zakatAmount)")

        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            "https://islam24.hazratdev.top/zakat".toUri()
        )

        val pendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val amountFormatted = formatCurrency(zakatAmount)
        val title = "🌙 Zakat Hawl Due Reminder"
        val message = "Your annual Zakat Hawl completion date has arrived. Estimated Zakat Payable: $amountFormatted. Tap to review your statement."

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val pattern = longArrayOf(0, 500, 1000)

        val notification = NotificationCompat.Builder(context, NotificationChannels.ZAKAT_CHANNEL_ID)
            .setSmallIcon(R.drawable.zakat)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setVibrate(pattern)
            .setContentIntent(pendingIntent)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(zakatId.hashCode(), notification)
            Log.d("ZakatNotificationReceiver", "Notification posted successfully for ID: $zakatId")
        } else {
            Log.w("ZakatNotificationReceiver", "POST_NOTIFICATIONS permission not granted. Cannot post notification.")
        }
    }
}
