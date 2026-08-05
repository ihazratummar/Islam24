package com.hazrat.utils



/**
 * Object containing constant values used in the application.
 */
object Constants {

    /** Base URL for retrieving the names of Allah */
    const val BASE_URL_NAME = "https://raw.githubusercontent.com/ihazratummar/99-Names-Of-Allah/main/"
    const val ATHKAR_BASE_URL_NAME = "https://raw.githubusercontent.com/ihazratummar/Muslim-Dua/refs/heads/main/"

    /** Base URL for the Aladhan API */
    const val PRAYER_BASE_URL = "https://api.aladhan.com/v1/hijriCalendar/"


    /** LocationOnCard IQ Api Key */
    const val LOCATION_IQ_BASE_URL = "https://us1.locationiq.com/v1/"
    const val LOCATION_IQ_API_KEY = BuildConfig.LOCATION_IQ_API_KEY


    /**Profile and Settings Constants */
    const val INTERNALSTORAGEPICTUREFOLDER = "profile_picture"
    const val PROFILE_PICTURE = "profile_picture"



    /*
    Storage Constants
     */

    const val PARENT_FOLDER_NAME_DOWNLOAD = "Download"
    const val DOWNLOADED_AZAN_FOLDER = "azans"
    const val SELECTED_ATHANS_SUB_FOLDER_NAME = "Selected Athan"


}
