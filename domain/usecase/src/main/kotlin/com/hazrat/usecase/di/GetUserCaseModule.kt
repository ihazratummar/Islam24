package com.hazrat.usecase.di

import com.hazrat.usecase.dua.GetDuaCategoryUseCase
import com.hazrat.usecase.dua.GetDuaItemListUseCase
import com.hazrat.usecase.prayer.GetDailyPrayerStatusUseCase
import com.hazrat.usecase.GetIslamicEventsUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetNextFridayTime
import com.hazrat.usecase.prayer.GetPrayerNotificationStateUseCase
import com.hazrat.usecase.prayer.GetPrayerTimeWindowForDaysUseCase
import com.hazrat.usecase.prayer.GetTodayPrayerTimeUseCase
import com.hazrat.usecase.GetUpcomingMainIslamicEventUseCase
import com.hazrat.usecase.dua.SearchAndGetDuaCategoriesUseCase
import com.hazrat.usecase.prayer.LogPrayerUseCase
import com.hazrat.usecase.prayer.PrayerNotificationEnabledUseCase
import com.hazrat.usecase.prayer.TogglePrayerUseCase
import com.hazrat.usecase.prayer.UnLogPrayerUseCase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.time.Clock


/**
 * @author hazratummar
 * Created on 13/05/26
 */

fun getUserCaseModule(): Module = module {
    single { GetTodayPrayerTimeUseCase(prayerTimeRepository = get()) }
    single { GetLocationNameUseCase(locationNameRepository = get(), prayerTimeRepository = get()) }
    single { GetUpcomingMainIslamicEventUseCase() }
    single { GetIslamicEventsUseCase(prayerTimeRepository = get()) }
    single { GetNextFridayTime(prayerTimeRepository = get()) }
    single { GetPrayerTimeWindowForDaysUseCase(prayerTimeRepository = get(),) }

    single<Clock> {
        Clock.systemDefaultZone()
    }
    single { LogPrayerUseCase(prayerLogRepository = get(), clock = get()) }
    single { UnLogPrayerUseCase(prayerLogRepository = get(), clock = get()) }
    single { GetDailyPrayerStatusUseCase(repository = get()) }
    single { TogglePrayerUseCase(logPrayer = get(), unLogPrayer = get()) }
    single { GetPrayerNotificationStateUseCase(prayerSettingRepository = get()) }
    single { PrayerNotificationEnabledUseCase(prayerSettingRepository = get()) }

    single { GetDuaCategoryUseCase(duaRepository = get()) }
    single { GetDuaItemListUseCase(duaRepository = get()) }
    single { SearchAndGetDuaCategoriesUseCase(duaRepository = get()) }

}