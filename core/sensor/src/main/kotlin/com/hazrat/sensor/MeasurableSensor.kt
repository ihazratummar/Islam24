package com.hazrat.sensor

/**
 * @author Hazrat Ummar Shaikh
 * Created on 24-01-2025
 */

abstract class MeasurableSensor(
    protected val sensorType: Int
) {

    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = null
    protected var onAccuracyChanged: ((Int) -> Unit)? = null

    abstract val doesSensorExist : Boolean

    abstract fun startListening()
    abstract fun stopListening()

    fun setOnSensorValuesChangedLister(listener: (List<Float>) -> Unit) {
        onSensorValuesChanged = listener
    }

    fun setOnAccuracyChangedListener(listener: (Int) -> Unit) {
        onAccuracyChanged = listener
    }

}