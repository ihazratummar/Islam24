package com.hazrat.notification.di

import androidx.core.app.NotificationManagerCompat
import com.hazrat.notification.MediaPlayerHelper
import com.hazrat.notification.NotificationChannels
import com.hazrat.notification.PrayerAlarmScheduler
import com.hazrat.notification.PrayerJanitorWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 24/01/26
 */

fun getNotificationModule () : Module = module {
    single<NotificationManagerCompat> { NotificationManagerCompat.from(get()) }
    single{ PrayerAlarmScheduler(context = get()) }
    single{ NotificationChannels(context = get()) }
    single{ MediaPlayerHelper(context = get()) }
    
    worker { PrayerJanitorWorker(get(), get()) }
}