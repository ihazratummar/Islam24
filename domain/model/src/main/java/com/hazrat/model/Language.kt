package com.hazrat.model

import androidx.annotation.StringRes
import com.hazrat.ui.R

/**
 * @author Hazrat Ummar Shaikh
 * Created on 19-06-2025
 */

enum class Languages {
    ENGLISH {
        override fun getString() = R.string.english
    },
    BENGALI {
        override fun getString() = R.string.bengali
    };

    @StringRes
    abstract fun getString(): Int
}