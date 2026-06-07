package com.hazrat.downloader

/**
 * @author Hazrat Ummar Shaikh
 * Created on 09-02-2025
 */

interface Downloader {

   suspend fun downloadFile(url: String, mimeType: String, title: String) : Long

}