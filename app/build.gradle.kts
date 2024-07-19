
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.hazrat.islam24"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hazrat.islam24"
        minSdk = 26
        targetSdk= 34
        versionCode = 34
        versionName = "1.3.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        configurations.all {
            resolutionStrategy { force ("androidx.work:work-runtime:2.9.0") }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    buildFeatures{
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v281)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.play.services.location)
    implementation(libs.transportation.consumer)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    implementation(libs.androidx.material.android)

    implementation(libs.androidx.material.icons.extended.android)

    //window size
    implementation(libs.androidx.window)

    //Splash Api
    implementation (libs.androidx.core.splashscreen)

    //Compose Navigation
    implementation (libs.androidx.navigation.compose)

    //Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //Retrofit
    implementation (libs.retrofit)
    implementation(libs.converter.gson)


    //Kotlinx Serialization
    implementation (libs.kotlinx.serialization.json)
    implementation (libs.retrofit2.kotlinx.serialization.converter)

    //Coil
    implementation(libs.coil.compose)

    //Datastore
    implementation (libs.androidx.datastore.preferences)

    //Compose Foundation
    implementation (libs.androidx.foundation)

    //Accompanist
    implementation (libs.accompanist.systemuicontroller)

    //paging3
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.paging.compose)

    //Room

    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

//    implementation (libs.accompanist.systemuicontroller.v0270)

    //Observe
    implementation (libs.androidx.activity.compose)
    implementation (libs.androidx.lifecycle.viewmodel.compose.v281)
    implementation (libs.androidx.runtime.livedata)

    implementation(libs.androidx.viewpager2)


    implementation (libs.androidx.runtime)
    implementation (libs.androidx.compose.ui.ui)
    implementation (libs.androidx.animation.core)

    implementation (libs.logging.interceptor)
    implementation(libs.onesignal)

    //Google Play Store Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    implementation(libs.review)
    implementation(libs.review.ktx)
}


