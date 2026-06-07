package com.hazrat.downloader.di

import android.content.Context
import com.hazrat.downloader.AndroidDownloader
import com.hazrat.downloader.Downloader
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 22/01/26
 */

fun getDownloadModule() : Module = module {
    single <Downloader>{ AndroidDownloader(context = get<Context>()) }
}