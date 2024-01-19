package com.hazrat.islam24.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hazrat.islam24.R
import com.hazrat.islam24.domain.model.PrayerTimingsStorage
import com.hazrat.islam24.utils.LocationUtils


class Prayertime : Fragment(){
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var locationNameTextView: TextView
    private lateinit var fajrTimeTextView: TextView
    private lateinit var dhuhrTimeTextView: TextView
    private lateinit var asharTimeTextView: TextView
    private lateinit var maghribTimeTextView: TextView
    private lateinit var ishaTimeTextView: TextView
    private lateinit var calendarView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateLocationName()
        fetchPrayerTimes()
        
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prayer_time, container, false)
        locationNameTextView =view.findViewById(R.id.p_location_name)
        fajrTimeTextView = view.findViewById(R.id.p_fajr_timer)
        dhuhrTimeTextView = view.findViewById(R.id.p_dhuhr_timer)
        asharTimeTextView= view.findViewById(R.id.p_ashar_timer)
        maghribTimeTextView= view.findViewById(R.id.p_maghrib_timer)
        ishaTimeTextView= view.findViewById(R.id.p_isha_timer)
        calendarView = view.findViewById(R.id.p_calendar_button)


        calendarView.setOnClickListener{
            val fragment = CalenderPageFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.addToBackStack("Prayertime")
            transaction?.replace(R.id.frame_layout, fragment)?.commit()
        }


        calendarView.setOnClickListener{
            val fragment = CalenderPageFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.addToBackStack("Prayertime")
            transaction?.replace(R.id.frame_layout, fragment)?.commit()
        }




        return view






    }


    private fun updateLocationName() {
        LocationUtils.getCityName(requireContext()) { cityName ->
            locationNameTextView.text = cityName
        }
    }
    private fun fetchPrayerTimes() {

        val prayerTimes = PrayerTimingsStorage.getCurrentPrayerTimings()
        Log.d("PrayerTimeFragment", "Times: "+prayerTimes)

        if (prayerTimes != null){
            Log.d("PrayerTimeFragments", "Times retrieved successfully: $prayerTimes")
            fajrTimeTextView.text = prayerTimes.fajr
            dhuhrTimeTextView.text= prayerTimes.dhuhr
            asharTimeTextView.text= prayerTimes.asr
            maghribTimeTextView.text= prayerTimes.maghrib
            ishaTimeTextView.text = prayerTimes.isha

        }
        else{
            Log.d("PrayerTimeFragment", "Prayer times are null")

        }

    }




}

