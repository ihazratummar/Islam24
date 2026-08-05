package com.hazrat.remote.di

import com.hazrat.remote.api.AthkarApiCall
import com.hazrat.remote.api.AthkarApiCallImpl
import com.hazrat.remote.api.AuthApiCall
import com.hazrat.remote.api.LocationNameApi
import com.hazrat.remote.api.LocationNameApiImpl
import com.hazrat.remote.api.NamesApi
import com.hazrat.remote.api.NamesApiImpl
import com.hazrat.remote.api.PrayerTimeApi
import com.hazrat.remote.api.PrayerTimeApiImpl
import com.hazrat.remote.api.apiData.ProfileApiImpl
import com.hazrat.remote.api.apiData.SupporterWebSocketApiImpl
import com.hazrat.remote.api.auth.AuthApiImple
import com.hazrat.remote.api.profile.ProfileApi
import com.hazrat.remote.api.profile.SupporterWebSocketApi
import com.hazrat.remote.clients.KtorClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * @author hazratummar
 * Created on 22/01/26
 */

const val PUBLIC_KTOR_CLIENT = "PUBLIC_KTOR_CLIENT"
const val CUSTOM_BACKEND = "CUSTOM_BACKEND"

fun getRemoteModule(): Module = module {

    // Public Ktor Client for external APIs (Aladhan, LocationIQ, Allah Names, Athkar)
    single(named(PUBLIC_KTOR_CLIENT)) { KtorClient.createPublicHttpClient() }

    // Authenticated Ktor Client for custom Islam24 backend testing
    single (named(CUSTOM_BACKEND)){ KtorClient.createHttpClient(tokenStorage = get()) }

    // API Services
    single<AthkarApiCall> { AthkarApiCallImpl(client = get(named(PUBLIC_KTOR_CLIENT))) }
    single<LocationNameApi> { LocationNameApiImpl(client = get(named(PUBLIC_KTOR_CLIENT))) }
    single<NamesApi> { NamesApiImpl(client = get(named(PUBLIC_KTOR_CLIENT))) }
    single<PrayerTimeApi> { PrayerTimeApiImpl(client = get(named(PUBLIC_KTOR_CLIENT))) }

    single <AuthApiCall>{ AuthApiImple(httpClient = get(named(CUSTOM_BACKEND))) }
    single <ProfileApi>{ ProfileApiImpl(httpClient = get(named(CUSTOM_BACKEND))) }
    single <SupporterWebSocketApi>{ SupporterWebSocketApiImpl(httpClient = get(named(CUSTOM_BACKEND))) }
}