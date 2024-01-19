package com.hazrat.islam24.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.hazrat.islam24.R
import com.hazrat.islam24.utils.LocationUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


class Home : Fragment() {
    private lateinit var salatTimeTextView: TextView
    private lateinit var locationNameTextView: TextView
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val htasbihButton: Button = view.findViewById(R.id._h_tasbih_button_shape)
        val hduasButton: Button = view.findViewById(R.id._h_duas_button_shape)
        val timelocationButton: Button = view.findViewById(R.id.timelocationButton)
        val hprayertimeButton : Button = view.findViewById(R.id._h_prayer_time_button_shape)
        val hzakatButton: Button =view.findViewById(R.id._h_zakat_button_shape)


        salatTimeTextView = view.findViewById(R.id.h_salat_timer)
        locationNameTextView =view.findViewById(R.id.location_name)

        ///call functions
        updateCurrentTime()
        updateLocationName()


        htasbihButton.setOnClickListener{
            val fragment =Tasbih()
            val transaction =fragmentManager?.beginTransaction()
            transaction?.addToBackStack("Home")
            transaction?.replace(R.id.frame_layout, fragment)?.commit()
        }

        timelocationButton.setOnClickListener{
            val fragment = Prayertime()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.addToBackStack("Home")
            transaction?.replace(R.id.frame_layout, fragment)?.commit()
        }

        hprayertimeButton.setOnClickListener{
            val fragment = Prayertime()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.addToBackStack("Home")
            transaction?.replace(R.id.frame_layout, fragment)?.commit()
        }

        hduasButton.setOnClickListener{
            val fragment = Duas()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.addToBackStack("Home")
            transaction?.replace(R.id.frame_layout,fragment)?.commit()
        }

        hzakatButton.setOnClickListener{
            val fragment = Zakat()
            val transaction =fragmentManager?.beginTransaction()
            transaction?.addToBackStack("Home")
            transaction?.replace(R.id.frame_layout, fragment)?.commit()
        }



//        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable{
            override fun run() {
                updateCurrentTime()
                handler.postDelayed(this, 1000)
            }
        },1000)

        return view
    }


    private fun updateCurrentTime(){
        val currentTime = getCurrentTime()
        salatTimeTextView.text = currentTime
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime():String{
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        return simpleDateFormat.format(calendar.time)
    }


    private fun updateLocationName() {
        LocationUtils.getCityName(requireContext()) { cityName ->
            locationNameTextView.text = cityName
        }
    }

}