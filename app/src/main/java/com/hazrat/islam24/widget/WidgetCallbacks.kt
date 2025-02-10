package com.hazrat.islam24.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState

/**
 * @author Hazrat Ummar Shaikh
 * Created on 20-01-2025
 */

class WuzuActionCallBack: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {

        updateAppWidgetState(context, glanceId){pref ->
            val currentKey = pref[WuzuWidget().wuzuKey]
            if (currentKey != null){
                pref[WuzuWidget().wuzuKey] = !currentKey
            }else{
                pref[WuzuWidget().wuzuKey] = false
            }
        }
        WuzuWidget().update(context, glanceId)
    }
}