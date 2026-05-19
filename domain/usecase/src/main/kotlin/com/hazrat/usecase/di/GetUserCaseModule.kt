package com.hazrat.usecase.di

import com.hazrat.usecase.GetDailyPrayerStatusUseCase
import com.hazrat.usecase.GetIslamicEventsUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetNextFridayTime
import com.hazrat.usecase.GetPrayerNotificationStateUseCase
import com.hazrat.usecase.GetTodayPrayerTimeUseCase
import com.hazrat.usecase.GetUpcomingMainIslamicEventUseCase
import com.hazrat.usecase.LogPrayerUseCase
import com.hazrat.usecase.PrayerNotificationEnabledUseCase
import com.hazrat.usecase.TogglePrayerUseCase
import com.hazrat.usecase.UnLogPrayerUseCase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.time.Clock


/**
 * @author hazratummar
 * Created on 13/05/26
 */

fun getUserCaseModule(): Module = module {
    single { GetTodayPrayerTimeUseCase(prayerTimeRepository = get()) }
    single { GetLocationNameUseCase(locationNameRepository = get()) }
    single { GetUpcomingMainIslamicEventUseCase() }
    single { GetIslamicEventsUseCase(prayerTimeRepositoryNew = get()) }
    single { GetNextFridayTime(prayerTimeRepositoryNew = get()) }

    single<Clock> {
        Clock.systemDefaultZone()
    }
    single { LogPrayerUseCase(prayerLogRepository = get(), clock = get()) }
    single { UnLogPrayerUseCase(prayerLogRepository = get(), clock = get()) }
    single { GetDailyPrayerStatusUseCase(repository = get()) }
    single { TogglePrayerUseCase(logPrayer = get(), unLogPrayer = get()) }
    single { GetPrayerNotificationStateUseCase(prayerSettingRepository = get()) }
    single { PrayerNotificationEnabledUseCase(prayerSettingRepository = get()) }
}