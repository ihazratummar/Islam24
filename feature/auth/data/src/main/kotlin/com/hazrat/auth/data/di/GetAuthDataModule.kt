package com.hazrat.auth.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.auth.data.repository.ForgetPasswordRepositoryImpl
import com.hazrat.auth.data.repository.ProfileRepositoryImpl
import com.hazrat.auth.data.repository.SyncRepositoryImpl
import com.hazrat.auth.domain.repository.ForgetPasswordRepository
import com.hazrat.auth.domain.repository.SyncRepository
import com.hazrat.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 24/01/26
 */

fun getAuthDataModule(): Module = module {

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    // FirebaseAuth
    single<FirebaseAuth> {
        FirebaseAuth.getInstance()
    }

    // FirebaseFirestore (if not already defined)
    single<FirebaseFirestore> {
        FirebaseFirestore.getInstance()
    }

    // FirebaseStorage (if not already defined)
    single<FirebaseStorage> {
        FirebaseStorage.getInstance()
    }

    single<ForgetPasswordRepository> {
        ForgetPasswordRepositoryImpl(
            api = get(),
            connectivityObserver = get()
        )
    }
    single<SyncRepository> {
        SyncRepositoryImpl(
            zakatRepository = get(),
            qiblaRepository = get()
        )
    }
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            context = get(),
            auth = get(),
            fireStore = get(),
            storage = get(),
            syncRepository = get(),
            connectivityObserver = get()
        )
    }
}