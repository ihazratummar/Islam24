package com.hazrat.auth.ui.di

import com.hazrat.auth.ui.forgetPassword.ForgetPasswordViewModel
import com.hazrat.auth.ui.login.LoginViewModel
import com.hazrat.auth.ui.signup.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 24/01/26
 */

fun getAuthUiModule(): Module = module {
    viewModel { LoginViewModel(get(), androidContext()) }
    viewModel { SignUpViewModel(context = androidContext(), profileRepository = get()) }
    viewModel { ForgetPasswordViewModel(forgetPasswordRepository = get()) }

}