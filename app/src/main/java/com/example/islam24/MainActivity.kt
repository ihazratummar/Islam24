package com.example.islam24

import com.example.islam24.utils.LocationStorage.setCurrentLocation
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.islam24.databinding.ActivityMainBinding
import com.example.islam24.domain.model.PrayerTimeHandler
import com.example.islam24.fragment.Home
import com.example.islam24.fragment.Prayertime
import com.example.islam24.fragment.CalenderPageFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)


        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.time -> replaceFragment(Prayertime())
                else ->{

                }
            }
            return@setOnNavigationItemSelectedListener true

        }
        PrayerTimeHandler.getPrayerTime(this) {}


        getCurrentLocation()

    }
    fun hideBottomNavigationView(){
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigationView(){
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager.beginTransaction() // Corrected the variable name
        fragmentManager.replace(R.id.frame_layout, fragment) // Corrected the variable name
        fragmentManager.commit() // Corrected the variable name
    }

    private fun getCurrentLocation()
    {
        if (checkPermissions())
        {
            if (isLocationEnabled())
            {
                ///getthe location
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task->
                    val location: Location?=task.result
                    if (location == null)
                    {
                        Toast.makeText(this, "No Location", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        /// store location
                        setCurrentLocation(location.latitude, location.longitude)
                        val latitude = location.latitude
                        val longitude = location.longitude
                        Log.e("LocationInfo","Location: $latitude $longitude")
                    }

                }
            }
            else
            {
                ///open settings to activate
                Toast.makeText(this, "Turn on Locatioon", Toast.LENGTH_SHORT).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
            }
        }
        else
        {
            ///requestpermissions
            requestPermission()
        }
    }

    private fun isLocationEnabled():Boolean{
        val locationManager:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object
    {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun checkPermissions(): Boolean
    {
        return (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else
            {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}