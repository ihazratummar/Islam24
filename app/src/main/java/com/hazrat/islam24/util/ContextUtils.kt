package com.hazrat.islam24.util

/**
 * @author Hazrat Ummar Shaikh
 */


import android.content.Context
import android.content.ContextWrapper
import javax.inject.Inject

class ContextUtils @Inject constructor(base: Context) : ContextWrapper(base) {

    companion object {
    }
}
