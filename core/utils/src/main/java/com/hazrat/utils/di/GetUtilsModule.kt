package com.hazrat.utils.di

import com.hazrat.utils.network.ConnectivityObserver
import com.hazrat.utils.network.NetworkConnectivityObserver
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 22/01/26
 */
 
fun getUtilsModule() : Module = module {

    single<ConnectivityObserver> { NetworkConnectivityObserver(context = get()) }
}