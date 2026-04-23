package com.hazrat.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

/**
 * @author Hazrat Ummar Shaikh
 * Created on 24-01-2025
 */

abstract class AndroidSensor(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int
) : MeasurableSensor(sensorType = sensorType), SensorEventListener {
    override val doesSensorExist: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun startListening() {
        if (!doesSensorExist) {
            return
        }
        if (!::sensorManager.isInitialized && sensor == null) {
            sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun stopListening() {
        if (!doesSensorExist || ::sensorManager.isInitialized) {
            return
        }
        sensorManager.unregisterListener(this)

    }
    private var lastUpdateTime: Long = 0

    override fun onSensorChanged(event: SensorEvent?) {
        if (!doesSensorExist){
            return
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime > 50){
            if (event?.sensor?.type == sensorType){
                Log.d("SensorValues", "Values: ${event.values.joinToString(",")}")
                onSensorValuesChanged?.invoke(event.values.toList())
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit
}