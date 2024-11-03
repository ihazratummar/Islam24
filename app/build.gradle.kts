
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.hazrat.islam24"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hazrat.islam24"
        minSdk = 26
        targetSdk= 35
        versionCode = 75
        versionName = "1.6.13"

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
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk.debugSymbolLevel = "FULL"
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
    ndkVersion = "26.1.10909125"


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v281)
    implementation(libs.androidx.activity.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    debugImplementation(libs.ui.tooling)
    implementation(libs.transportation.consumer)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit.v115)
    implementation(libs.androidx.material.icons.extended.android)





    implementation(libs.play.services.location)

    /*
    FireBase
     */
    implementation (libs.play.services.auth)
    implementation(libs.firebase.auth.ktx)
    implementation (libs.firebase.database)
    platform(libs.firebase.bom.v2821)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)


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


    //Observe
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

    //dataStorePreference
    implementation(libs.androidx.preference.ktx)
}
