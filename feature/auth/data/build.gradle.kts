import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.hazrat.auth.data"
    compileSdk {
        version = release(37)
    }

    buildFeatures {
        buildConfig = true
    }

    val localProperties = Properties()
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists() && localPropertiesFile.isFile){
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }
    }

    defaultConfig {
        minSdk = 26

        buildConfigField(
            "String",
            "REVENUECAT_API_KEY",
            localProperties.getProperty("REVENUECAT_API_KEY")
        )

        buildConfigField(
            "String",
            "GOOGLE_SIGN_WEB_SDK_CLIENT",
            localProperties.getProperty("GOOGLE_SIGN_WEB_SDK_CLIENT")
        )
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
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))

    implementation(project(":domain:repository"))

    implementation(project(":feature:zakat"))
    implementation(libs.revenuecat.purchases)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.koin.compose)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}