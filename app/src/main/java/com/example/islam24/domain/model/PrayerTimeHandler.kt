package com.example.islam24.domain.model

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.islam24.domain.model.PrayerTimingsStorage.setCurrentPrayerTimings
import com.example.islam24.utils.LocationStorage
import org.json.JSONObject
import java.text.SimpleDateFormat

object PrayerTimeHandler {

    private var prayerTimings: PrayerTimingsData? =null

    fun getPrayerTime(context: Context,onPrayerTimingsLoaded: (PrayerTimingsData) -> Unit) {
        val currentLocation = LocationStorage.getCurrentLocation()

        if (currentLocation != null) {
            //get latitude & longitude
            val latitude = currentLocation.latitude
            val longitude = currentLocation.longitude

            ////get time date
            val time = Calendar.getInstance().time
            val getYear = SimpleDateFormat("yyyy")
            val getMonth = SimpleDateFormat("MM")
            val getDay = SimpleDateFormat("dd")

            val year = getYear.format(time)
            val month = getMonth.format(time)
            val today = getDay.format(time)

            val queue = Volley.newRequestQueue(context)
            val url =
                "http://api.aladhan.com/v1/calendar/$year/$month?latitude=$latitude&longitude=$longitude&method=1"

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
//                Log.e("Response", "getData:" + response.toString())
                    val jsonObject = JSONObject(response.toString())
                    val dataArray = jsonObject.getJSONArray("data")


                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)
                        val dateObject = dataObject.getJSONObject("date")
                        val apiday = dateObject.getJSONObject("gregorian").getString("day")

                        if (apiday == today) {
                            val timing = dataObject.getJSONObject("timings")

                            val fajr = timing.get("Fajr").toString()
                            val dhuhr = timing.get("Dhuhr").toString()
                            val asr = timing.get("Asr").toString()
                            val maghrib = timing.get("Maghrib").toString()
                            val isha = timing.get("Isha").toString()
                            Log.e(
                                "PrayerTimesAll",
                                "Fajr Time: " + fajr.toString() + "Dhuhr: " + dhuhr.toString() + "Asr: " + asr.toString() + "Maghrib: " + maghrib.toString() + "Isha: " + isha.toString()
                            )
                            prayerTimings = setCurrentPrayerTimings(fajr, dhuhr, asr, maghrib, isha)
                            onPrayerTimingsLoaded(prayerTimings!!)
                        }

                    }
                },
                { error ->
                    Toast.makeText(context, "${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                })
            queue.add(stringRequest)
        }

        fun getPrayerTimings(): PrayerTimingsData? {
            return prayerTimings
        }
    }

}