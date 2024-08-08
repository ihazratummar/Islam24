package com.hazrat.islam24.core.domain.repository


import com.hazrat.islam24.core.domain.model.hijricalendar.HijriCalendarResponse
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.core.data.entity.HijriCalendarEntity
import kotlinx.coroutines.flow.Flow

interface HijriCalendarRepository {

    /**
     * Fetches the Hijri calendar data from an external API.
     * This function retrieves the Hijri calendar data from a remote source.
     *
     * @return A HijriCalendarResponse object containing the Hijri calendar data, or null if the request fails.
     */
    suspend fun getHijriCalendarFromApi(): HijriCalendarResponse?

    /**
     * Retrieves all Gregorian to Hijri conversion entities from the database.
     *
     * @return A Flow representing a list of GregorianToHijriEntity objects.
     */
    fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>>

    /**
     * Inserts a Hijri calendar entity into the database.
     * If an entity with the same primary key already exists, it will be replaced.
     *
     * @param hijriCalendarList The HijriCalendarEntity object to be inserted.
     * @return The HijriCalendarEntity object that was inserted.
     */
    suspend fun insertCalendarList(hijriCalendarList: HijriCalendarEntity): HijriCalendarEntity

    /**
     * Retrieves all Hijri calendar entities from the database.
     *
     * @return A Flow representing a list of HijriCalendarEntity objects.
     */
    fun getCalendarList(): Flow<List<HijriCalendarEntity>>
}
