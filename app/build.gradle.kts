import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    alias(libs.plugins.kotlin.android)
}



android {
    namespace = "com.hazrat.islam24"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.hazrat.islam24"
        minSdk = 26
        this.targetSdk = 36
        versionCode = 98
        versionName = "3.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "MY_PASS_PHRASE", properties.getProperty("MY_PASS_PHRASE"))
        buildConfigField("String", "MAPS_API_KEY", properties.getProperty("MAPS_API_KEY"))

        vectorDrawables {
            useSupportLibrary = true
        }
        configurations.all {
            resolutionStrategy { force ("androidx.work:work-runtime:2.10.0") }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    buildFeatures{
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ndkVersion = "29.0.14033849 rc4"
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {

    implementation(project(":core:ui"))
    implementation(project(":core:utils"))
    implementation(project(":core:sensor"))
    implementation(project(":core:remote"))
    implementation(project(":core:location"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:downloader"))
    implementation(project(":core:permission"))
    implementation(project(":core:notification"))

    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))
    implementation(project(":domain:repository"))

    implementation(project(":feature:zakat"))
    implementation(project(":feature:common"))
    implementation(project(":feature:calendar"))

    implementation(project(":feature:auth:ui"))
    implementation(project(":feature:auth:data"))
    implementation(project(":feature:auth:domain"))

    implementation(project(":feature:qibla:ui"))
    implementation(project(":feature:qibla:data"))

    implementation(project(":feature:allahNames:ui"))
    implementation(project(":feature:allahNames:data"))
    implementation(project(":feature:allahNames:domain"))

    implementation(project(":feature:athkar:ui"))
    implementation(project(":feature:athkar:data"))
    implementation(project(":feature:athkar:domain"))

    implementation(project(":feature:alQuran:ui"))
    implementation(project(":feature:alQuran:data"))
    implementation(project(":feature:alQuran:domain"))

    implementation(project(":feature:prayertime:ui"))
    implementation(project(":feature:prayertime:data"))

    implementation(project(":feature:home:ui"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.koin.workmanager)

    //window size
    implementation(libs.androidx.window)

    //Splash Api
    implementation (libs.androidx.core.splashscreen)

    //Compose Navigation
    implementation (libs.androidx.navigation.compose)

    //koin
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.navigation)

    //Kotlinx Serialization
    implementation (libs.kotlinx.serialization.json)
    implementation (libs.retrofit2.kotlinx.serialization.converter)

    //Compose Foundation
    implementation (libs.androidx.foundation)

    //paging3
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.paging.compose)

    //Observe
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.runtime.livedata)

    //Google Play Store Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    implementation(libs.review)
    implementation(libs.review.ktx)

    //Glance App Widget

    implementation (libs.androidx.glance)
    implementation (libs.androidx.glance.appwidget)

    implementation(libs.timber)
}