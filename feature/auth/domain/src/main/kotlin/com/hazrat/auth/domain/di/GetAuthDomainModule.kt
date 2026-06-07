package com.hazrat.auth.domain.di

import com.hazrat.auth.domain.usecase.LoginUseCase
import com.hazrat.auth.domain.usecase.ObserveAuthStateUseCase
import com.hazrat.auth.domain.usecase.ObserveUserUseCase
import com.hazrat.auth.domain.usecase.SignUpUseCase
import com.hazrat.auth.domain.usecase.SignOutUseCase
import com.hazrat.auth.domain.usecase.UpdateBioUseCase
import com.hazrat.auth.domain.usecase.UpdateNameUseCase
import com.hazrat.auth.domain.usecase.UpdateProfilePictureUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author Hazrat Ummar Shaikh
 */

fun getAuthDomainModule(): Module = module {
    factory { LoginUseCase(get()) }
    factory { ObserveAuthStateUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { ObserveUserUseCase(get()) }
    factory { UpdateNameUseCase(get()) }
    factory { UpdateBioUseCase(get()) }
    factory { UpdateProfilePictureUseCase(get()) }
}