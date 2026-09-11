
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.room) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}

configurations.all {
    resolutionStrategy{
        force (libs.androidx.compose.bom)
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        val sanitizedPath = project.path.removePrefix(":").replace(":", "_")
        val baseName = if (sanitizedPath.isEmpty()) "root" else sanitizedPath
        compilerOptions {
            moduleName.set("${project.rootProject.name.replace("-", "_")}_$baseName")
        }
    }
}