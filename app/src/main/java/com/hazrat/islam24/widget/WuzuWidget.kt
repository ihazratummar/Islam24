package com.hazrat.islam24.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.R

/**
 * @author Hazrat Ummar Shaikh
 * Created on 20-01-2025
 */


class WuzuWidget : GlanceAppWidget() {

    val wuzuKey = booleanPreferencesKey("Wuzu")
    @SuppressLint("RestrictedApi")
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            val wuzuState = currentState(key = wuzuKey) == true
            Column(
                modifier = GlanceModifier
                    .padding(dimens.size5),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Box (
                    modifier = GlanceModifier.size(dimens.size80)
                        .clickable(
                            onClick = actionRunCallback(WuzuActionCallBack::class.java)
                        )
                        .cornerRadius(dimens.size80),
                    contentAlignment = Alignment.Center
                ){
                    val imageProvider = if (wuzuState) R.drawable.prayers else R.drawable.ablution
                    Image(
                        provider = ImageProvider(imageProvider),
                        contentDescription = null,
                        modifier = GlanceModifier.size(dimens.size40),
                    )
                }
                Text(
                    modifier = GlanceModifier.padding(
                        bottom = dimens.size5
                    ),
                    text = if (wuzuState) "Wuzu" else "No Wuzu",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color.White),
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

