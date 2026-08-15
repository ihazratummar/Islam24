package com.hazrat.database.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import java.util.UUID
import java.util.UUID.fromString
import kotlin.time.Instant.Companion.parse


/**
 * @author hazratummar
 * Created on 07/08/26
 */

class DatabaseConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUUID(value: String?): UUID? = value?.let(UUID::fromString)

    @TypeConverter
    fun fromInstant(instant: Instant?): String? =
        instant?.toString()

    @TypeConverter
    fun toInstant(value: String?): Instant? =
        value?.let(Instant::parse)

    @TypeConverter
    fun fromLocalDate(date: kotlinx.datetime.LocalDate?): String? =
        date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): kotlinx.datetime.LocalDate? =
        value?.let { kotlinx.datetime.LocalDate.parse(it) }

}