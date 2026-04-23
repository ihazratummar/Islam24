package com.hazrat.model.al_quran_model.local_quran_ar

import android.content.Context
import com.google.gson.Gson

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-12-2024
 */

class LocalQuranModelAr : ArrayList<LocalQuranModelArItem>() {
    fun getQuranAr(context: Context, fileName: String): List<LocalQuranModelArItem>{
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val localQuranDataItemList = Gson().fromJson(jsonString, LocalQuranModelAr::class.java)

        return localQuranDataItemList
    }
}