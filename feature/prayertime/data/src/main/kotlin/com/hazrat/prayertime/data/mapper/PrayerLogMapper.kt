package com.hazrat.prayertime.data.mapper

import com.hazrat.database.entity.PrayerLogEntity
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.Prayer
import java.time.LocalDate


/**
 * @author hazratummar
 * Created on 18/05/26
 */
 

object PrayerLogMapper {

    fun toEntity(
        date: LocalDate,
        prayer: Prayer
    ): PrayerLogEntity{
        return PrayerLogEntity(
            date = date.toString(),
            prayer = prayer.name,
        )
    }

    fun toDailyStatusList(date: LocalDate, prayers: List<PrayerLogEntity>) : DailyPrayerStatus {

        val prayersName = prayers.map { prayers->
            Prayer.valueOf(prayers.prayer)
        }.toSet()

        return DailyPrayerStatus(
            date = date,
            loggedPrayers = prayersName
        )
    }
}