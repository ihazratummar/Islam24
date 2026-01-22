package com.hazrat.islam24.auth.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

/**
 * @author Hazrat Ummar Shaikh
 */

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    class  StringResource(
        @param:StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText()

    @Composable
    fun asString(): String{
        return when(this){
            is DynamicString -> value
            is StringResource -> stringResource(id, *args)
        }
    }

    fun asString(context: Context): String{
        return when(this){
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
    }
}