package com.hazrat.islam24.core.domain.repository


import com.hazrat.islam24.core.domain.model.gregoriantohijri.GregorianToHijriResponse
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import kotlinx.coroutines.flow.Flow

interface GregorianToHijriRepository {

    /**
     * Fetches the Gregorian to Hijri date conversion.
     * This function retrieves the converted date from an external source.
     *
     * @return A GregorianToHijriResponse object representing the converted date.
     */
    suspend fun getGregorianToHijriDate(): GregorianToHijriResponse?

    /**
     * Retrieves all Gregorian to Hijri conversion entities from the database.
     *
     * @return A Flow representing a list of GregorianToHijriEntity objects.
     */
    fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>>
}
