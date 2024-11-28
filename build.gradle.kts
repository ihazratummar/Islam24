// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.room) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    dependencies {
        classpath(libs.google.services)
        classpath(libs.onesignal.gradle.plugin)
        classpath(libs.compose.compiler.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
    }
}

configurations.all {
    resolutionStrategy{
        force (libs.androidx.compose.bom)
    }
}