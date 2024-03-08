package com.hazrat.islam24.domain.repository


import com.hazrat.hijricaneldar.domain.model.hijricalendar.HijriCalendarResponse
import com.hazrat.islam24.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.data.entity.HijriCalendarEntity
import kotlinx.coroutines.flow.Flow

interface HijriCalendarRepository {

    suspend fun getHijriCalendarFromApi(): HijriCalendarResponse?

    fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>>

    suspend fun insertCalendarList(hijriCalendarList: HijriCalendarEntity): HijriCalendarEntity

    fun getCalendarList(): Flow<List<HijriCalendarEntity>>

}