package com.hazrat.islam24.core.remote.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-12-2024
 */

interface QuranApi {

    @GET("quran_ar.json")
    suspend fun downloadQuranArFile() : Response<ResponseBody>

    @GET("quran_transliteration.json")
    suspend fun downloadQuranTransliterationFile() : Response<ResponseBody>

    @GET("quran_en.json")
    suspend fun downloadQuranEnFile() : Response<ResponseBody>

    @GET("quran_bn.json")
    suspend fun downloadQuranBnFile() : Response<ResponseBody>


}