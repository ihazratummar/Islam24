package com.hazrat.prayertime.data.mapper

import com.hazrat.database.entity.prayer.PrayerLogEntity
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.Prayer
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.time.LocalDate

/**
 * Clean mapper between PrayerLogEntity database model and DailyPrayerStatus domain model.
 * @author hazratummar
 */
object PrayerLogMapper {

    fun toDailyStatus(date: LocalDate, entity: PrayerLogEntity?): DailyPrayerStatus {
        if (entity == null) {
            return DailyPrayerStatus(date = date, loggedPrayers = emptySet())
        }
        val logged = mutableSetOf<Prayer>()
        if (entity.fajr) logged.add(Prayer.FAJR)
        if (entity.dhuhr) logged.add(Prayer.DHUHR)
        if (entity.asr) logged.add(Prayer.ASR)
        if (entity.maghrib) logged.add(Prayer.MAGHRIB)
        if (entity.isha) logged.add(Prayer.ISHA)

        return DailyPrayerStatus(
            date = date,
            loggedPrayers = logged
        )
    }

    fun toEntity(
        date: kotlinx.datetime.LocalDate,
        prayer: Prayer,
        isLogged: Boolean,
        existing: PrayerLogEntity? = null,
        now: Instant? = null
    ): PrayerLogEntity {
        val updatedAtInstant = now ?: Instant.fromEpochMilliseconds(System.currentTimeMillis())
        return PrayerLogEntity(
            logDate = date,
            fajr = if (prayer == Prayer.FAJR) isLogged else (existing?.fajr ?: false),
            dhuhr = if (prayer == Prayer.DHUHR) isLogged else (existing?.dhuhr ?: false),
            asr = if (prayer == Prayer.ASR) isLogged else (existing?.asr ?: false),
            maghrib = if (prayer == Prayer.MAGHRIB) isLogged else (existing?.maghrib ?: false),
            isha = if (prayer == Prayer.ISHA) isLogged else (existing?.isha ?: false),
            isSynced = false,
            updatedAt = updatedAtInstant
        )
    }
}