package com.hazrat.prayertime.data.mapper

import com.hazrat.model.EventType


/**
 * @author hazratummar
 * Created on 16/05/26
 */

fun String.toEventType(): EventType {

    return when {

        ursKeywords.any {
            contains(it, ignoreCase = true)
        } -> {
            EventType.URS
        }

        birthdayKeywords.any {
            contains(it, ignoreCase = true)
        } -> {
            EventType.BIRTHDAY
        }

        nightPrayerKeywords.any {
            contains(it, ignoreCase = true)
        } -> {
            EventType.NIGHT_PRAYER
        }

        hajjKeywords.any{
            contains(it, ignoreCase = true)
        } -> {EventType.HAJJ}

        else -> {
            EventType.SPECIAL
        }
    }
}

private val ursKeywords = listOf(
    "urs"
)

private val birthdayKeywords = listOf(
    "birthday",
    "birth",
    "mawlid"
)

private val nightPrayerKeywords = listOf(
    "night",
    "laylat",
    "qadr"
)

private val hajjKeywords = listOf(
    "hajj"
)