package com.hazrat.islam24.widget.di

import com.hazrat.domain.repository.WidgetUpdater
import com.hazrat.islam24.widget.GlanceWidgetUpdater
import com.hazrat.islam24.widget.PrayerWidgetDatabaseObserver
import org.koin.core.module.Module
import org.koin.dsl.module

fun getWidgetModule(): Module = module {
    single<WidgetUpdater> { GlanceWidgetUpdater(context = get()) }
    single { PrayerWidgetDatabaseObserver(prayerLogDao = get(), widgetUpdater = get()) }
}