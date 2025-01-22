package com.hazrat.islam24.util

import com.hazrat.islam24.BuildConfig


/**
 * Object containing constant values used in the application.
 */
object Constants {

    /** Base URL for retrieving the names of Allah */
    const val BASE_URL_NAME = "https://raw.githubusercontent.com/ihazratummar/99-Names-Of-Allah/main/"
    const val ATHKAR_BASE_URL_NAME = "https://raw.githubusercontent.com/ihazratummar/Muslim-Dua/refs/heads/main/"

    /** Base URL for the Aladhan API */
    const val BASE_URL = "https://api.aladhan.com/v1/calendar/"

    /** Location IQ Api Key */
    const val LOCATION_IQ_BASE_URL = "https://us1.locationiq.com/v1/"
    const val LOCATION_IQ_API_KEY = BuildConfig.LOCATION_IQ_API_KEY

    /** Base URL for retrieving Gregorian to Hijri conversion data */
    const val GTH_BASE_URL = "https://api.aladhan.com/"

    /** Base URL for retrieving Hijri calendar data */
    const val HIJRI_CALENDAR_URL = "https://api.aladhan.com/"


    const val NISAB_DATABASE_NAME = "nisab_db"
    const val KEY_SORT_BY = "sort_type"


    /**Profile and Settings Constants */
    const val INTERNALSTORAGEPICTUREFOLDER = "profile_picture"
    const val PROFILE_PICTURE = "profile_picture"

    /** Quran Api Base  Url  */
    const val QURAN_AR_BASE_URL = "https://raw.githubusercontent.com/ihazratummar/AlQuran/refs/heads/main/"



    /*
    Storage Constants
     */

    const val PARENT_FOLDER_NAME_DOWNLOAD = "Download"
    const val SELECTED_ATHANS_SUB_FOLDER_NAME = "Selected Athan"


    /*
    DataStore Constants
     */

    const val APP_DATA_STORE = "APP_DATA_STORE"

    const val USER_DATA_SORE = "USER_DATA_SORE"

    /*
    Islam 24 Backend
     */

    const val RENDER_BASE_URL = "https://islam24-backend.onrender.com/"

}
