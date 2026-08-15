package com.hazrat.usecase.di

import com.hazrat.usecase.GetIslamicEventsUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetNextFridayTime
import com.hazrat.usecase.GetUpcomingMainIslamicEventUseCase
import com.hazrat.usecase.profile.GetProfileDataUseCase
import com.hazrat.usecase.profile.GetRecentTickersUseCase
import com.hazrat.usecase.profile.GetSupporterStatusUseCase
import com.hazrat.usecase.profile.GoogleSignInUseCase
import com.hazrat.usecase.profile.IsLoggedInUseCase
import com.hazrat.usecase.profile.IsSubscribedUseCase
import com.hazrat.usecase.profile.SignOutUseCase
import com.hazrat.usecase.profile.SyncSupporterStatusUseCase
import com.hazrat.usecase.dua.DeleteRecentDuaUseCase
import com.hazrat.usecase.dua.GetAllChaptersUseCase
import com.hazrat.usecase.dua.GetBookmarkedDuasUseCase
import com.hazrat.usecase.dua.GetCategoryChaptersUseCase
import com.hazrat.usecase.dua.GetDuaCategoryUseCase
import com.hazrat.usecase.dua.GetDuaItemListUseCase
import com.hazrat.usecase.dua.GetRecentDuasUseCase
import com.hazrat.usecase.dua.SaveRecentDuaUseCase
import com.hazrat.usecase.dua.SearchAndGetDuaCategoriesUseCase
import com.hazrat.usecase.dua.SearchChaptersUseCase
import com.hazrat.usecase.dua.ToggleDuaBookmarkUseCase
import com.hazrat.usecase.khatam.EndKhatamPlanUseCase
import com.hazrat.usecase.khatam.GetActiveKhatamPlanUseCase
import com.hazrat.usecase.khatam.GetKhatamHistoryUseCase
import com.hazrat.usecase.khatam.ResetKhatamPlanUseCase
import com.hazrat.usecase.khatam.StartKhatamPlanUseCase
import com.hazrat.usecase.khatam.UpdateKhatamProgressUseCase
import com.hazrat.usecase.khatam.UpdateKhatamTargetDateUseCase
import com.hazrat.usecase.prayer.GetDailyPrayerStatusUseCase
import com.hazrat.usecase.prayer.GetPrayerTimeWindowForDaysUseCase
import com.hazrat.usecase.prayer.GetTodayPrayerTimeUseCase
import com.hazrat.usecase.prayer.LogPrayerUseCase
import com.hazrat.usecase.prayer.PrayerNotificationEnabledUseCase
import com.hazrat.usecase.prayer.RefreshPrayerTimeUseCase
import com.hazrat.usecase.prayer.TogglePrayerUseCase
import com.hazrat.usecase.prayer.UnLogPrayerUseCase
import com.hazrat.usecase.prayer.UpdateCalculationMethodUseCase
import com.hazrat.usecase.prayer.UpdateJuristicMethodUseCase
import com.hazrat.usecase.prayer.UpdatePrayerAudioUseCase
import com.hazrat.usecase.prayer.UpdatePrayerOffsetMinuteUseCase
import com.hazrat.usecase.prayer.UserPrayerSettingUseCase
import com.hazrat.usecase.profile.ListenToSupportTickerUseCase
import com.hazrat.usecase.profile.SaveTickerUseCase
import com.hazrat.usecase.profile.SyncDataUseCase
import com.hazrat.usecase.quran.ControlQuranAudioUseCase
import com.hazrat.usecase.quran.DeleteRecentSurahUseCase
import com.hazrat.usecase.quran.GetAllSurahListUseCase
import com.hazrat.usecase.quran.GetBookmarkedAyahsUseCase
import com.hazrat.usecase.quran.GetRecentSurahsUseCase
import com.hazrat.usecase.quran.GetSurahAyahsUseCase
import com.hazrat.usecase.quran.SaveRecentSurahUseCase
import com.hazrat.usecase.quran.ToggleAyahBookmarkUseCase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.time.Clock

/**
 * @author hazratummar
 * Created on 13/05/26
 */

fun getUserCaseModule(): Module = module {
    single { GetTodayPrayerTimeUseCase(prayerTimeRepository = get()) }
    single { RefreshPrayerTimeUseCase(prayerTimeRepository = get()) }
    single { GetLocationNameUseCase(locationNameRepository = get(), prayerTimeRepository = get()) }
    single { GetUpcomingMainIslamicEventUseCase() }

    single { GetIslamicEventsUseCase(prayerTimeRepository = get()) }
    single { GetNextFridayTime(prayerTimeRepository = get()) }
    single { GetPrayerTimeWindowForDaysUseCase(prayerTimeRepository = get()) }
    single { UpdateCalculationMethodUseCase(prayerSettingRepository = get()) }
    single { UpdateJuristicMethodUseCase(prayerSettingRepository = get()) }
    single { UserPrayerSettingUseCase(prayerSettingRepository = get()) }
    single { UpdatePrayerAudioUseCase(prayerSettingRepository = get()) }
    single { UpdatePrayerOffsetMinuteUseCase(prayerSettingRepository = get()) }

    single<Clock> {
        Clock.systemDefaultZone()
    }
    single { LogPrayerUseCase(prayerLogRepository = get(), clock = get()) }
    single { UnLogPrayerUseCase(prayerLogRepository = get(), clock = get()) }
    single { GetDailyPrayerStatusUseCase(repository = get()) }
    single { TogglePrayerUseCase(logPrayer = get(), unLogPrayer = get()) }
    single { PrayerNotificationEnabledUseCase(prayerSettingRepository = get()) }

    single { GetDuaCategoryUseCase(duaRepository = get()) }
    single { GetDuaItemListUseCase(duaRepository = get()) }
    single { SearchAndGetDuaCategoriesUseCase(duaRepository = get()) }
    single { GetCategoryChaptersUseCase(duaRepository = get()) }
    single { GetAllChaptersUseCase(duaRepository = get()) }
    single { SearchChaptersUseCase(duaRepository = get()) }
    single { GetBookmarkedDuasUseCase(duaRepository = get()) }
    single { ToggleDuaBookmarkUseCase(duaRepository = get()) }
    single { SaveRecentDuaUseCase(duaRepository = get()) }
    single { GetRecentDuasUseCase(duaRepository = get()) }
    single { DeleteRecentDuaUseCase(duaRepository = get()) }

    single { GetAllSurahListUseCase(quranRepository = get()) }
    single { GetSurahAyahsUseCase(quranRepository = get()) }
    single { GetRecentSurahsUseCase(quranRepository = get()) }
    single { SaveRecentSurahUseCase(quranRepository = get()) }
    single { DeleteRecentSurahUseCase(quranRepository = get()) }
    single { ControlQuranAudioUseCase(audioPlaybackRepository = get()) }
    single { ToggleAyahBookmarkUseCase(quranRepository = get()) }
    single { GetBookmarkedAyahsUseCase(quranRepository = get()) }

    single { GetActiveKhatamPlanUseCase(repository = get()) }
    single { GetKhatamHistoryUseCase(repository = get()) }
    single { StartKhatamPlanUseCase(repository = get()) }
    single { UpdateKhatamProgressUseCase(repository = get()) }
    single { UpdateKhatamTargetDateUseCase(repository = get()) }
    single { ResetKhatamPlanUseCase(repository = get()) }
    single { EndKhatamPlanUseCase(repository = get()) }

    // Auth
    single { GoogleSignInUseCase(get()) }
    single { SignOutUseCase(authRepository = get(), profileRepository = get()) }
    single { IsLoggedInUseCase(get()) }
    single { IsSubscribedUseCase(userDataStore = get()) }
    single { GetProfileDataUseCase(get()) }
    single { GetSupporterStatusUseCase(profileRepository = get()) }
    single { SyncSupporterStatusUseCase(profileRepository = get()) }

    single { ListenToSupportTickerUseCase(get()) }
    single { GetRecentTickersUseCase(get()) }
    single { SaveTickerUseCase(get()) }

    single { SyncDataUseCase(get()) }




}