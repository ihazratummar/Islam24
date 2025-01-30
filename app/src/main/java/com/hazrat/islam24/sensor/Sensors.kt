package com.hazrat.islam24.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * @author Hazrat Ummar Shaikh
 * Created on 24-01-2025
 */

data class RotationSensor(
    val context: Context,
): AndroidSensor(
    context = context,
    sensorFeature = "",
    sensorType = Sensor.TYPE_ROTATION_VECTOR
){
    override val doesSensorExist: Boolean
        get(){
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            return sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null
        }
}


class CompassSensor(
    val context: Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_COMPASS,
    sensorType = Sensor.TYPE_MAGNETIC_FIELD
){
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    override val doesSensorExist: Boolean
        get() = magneticField != null && accelerometer != null

    // Define sensorEventListener as a private property
    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    System.arraycopy(event.values, 0, gravity, 0, event.values.size)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
                }
            }

            // Compute azimuth (direction) if both sensors are available
            if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                SensorManager.getOrientation(rotationMatrix, orientationAngles)
                val azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                val normalizedAzimuth = (azimuth + 360) % 360 // Normalize to 0-360
                onSensorValuesChanged?.invoke(listOf(normalizedAzimuth))
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }


    override fun startListening() {
        if (doesSensorExist) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
            sensorManager.registerListener(sensorEventListener, magneticField, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun stopListening() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}