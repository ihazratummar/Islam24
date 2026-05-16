package com.hazrat.usecase.di

import com.hazrat.usecase.GetIslamicEventsUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetTodayPrayerTimeUseCase
import com.hazrat.usecase.GetUpcomingIslamicEventUseCase
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 13/05/26
 */

fun getUserCaseModule(): Module = module {
    single { GetTodayPrayerTimeUseCase(prayerTimeRepository = get()) }
    single { GetLocationNameUseCase(locationNameRepository = get()) }
    single { GetUpcomingIslamicEventUseCase() }
    single { GetIslamicEventsUseCase(prayerTimeRepositoryNew = get()) }
}