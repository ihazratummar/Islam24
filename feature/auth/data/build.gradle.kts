import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.hazrat.auth.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}


dependencies {
    implementation(project(":core:utils"))
    implementation(project(":core:remote"))

    implementation(project(":domain:repository"))

    implementation(project(":feature:zakat"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation (libs.koin.compose)

    /*
    FireBase
     */
    implementation (libs.play.services.auth)

    api(libs.firebase.auth.ktx)
    api (libs.firebase.database)
    platform(libs.firebase.bom)
    api(libs.firebase.firestore)
    api(libs.firebase.storage)
}