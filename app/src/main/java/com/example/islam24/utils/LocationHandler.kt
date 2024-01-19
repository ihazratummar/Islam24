package com.example.islam24.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.DelicateCoroutinesApi
import org.json.JSONObject

object LocationUtils {

    @OptIn(DelicateCoroutinesApi::class)
    fun getCityName(context: Context, callback: (String) -> Unit) {
        // Fetch the current location
        val currentLocation = LocationStorage.getCurrentLocation()

        if (currentLocation != null) {
            val latitude = currentLocation.latitude
            val longitude = currentLocation.longitude

            val queue = Volley.newRequestQueue(context)

            val apiUrl = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$latitude&lon=$longitude"

            val stringRequest = StringRequest(
                Request.Method.GET, apiUrl,
                { response ->
//                    Log.e("Response", "getData: $response")

                    try {
                        val jsonObject = JSONObject(response)
                        val address = jsonObject.getJSONObject("address")
                        val cityName = address.optString("city", address.optString("village", "Unknown"))

                        // Use the callback to return the city name
                        callback(cityName)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback("Unknown")
                    }

                },
                { error ->
                    Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    callback("Unknown")
                })

            queue.add(stringRequest)
        }
    }
}
