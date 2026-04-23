package com.hazrat.permission

import android.Manifest
import android.os.Build

object PermissionTypes {

    const val LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    const val CAMERA = Manifest.permission.CAMERA

    val NOTIFICATION : String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.POST_NOTIFICATIONS
        else null

}