# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.moshi.** { *; }
-keep class com.google.gson.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn com.squareup.moshi.**
-dontwarn com.google.gson.**


# Room
-keep class androidx.room.** { *; }
-keep class androidx.sqlite.db.** { *; }
-keep class androidx.arch.core.executor.** { *; }
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.room.**
-dontwarn androidx.sqlite.db.**
-dontwarn androidx.arch.core.executor.**
-dontwarn androidx.lifecycle.**

# Keep classes accessed via reflection
-keep class * { @androidx.room.Entity *; }
-keep class * { @androidx.room.Dao *; }


# Keep annotation classes
-keep @interface javax.inject.** { *; }
-keep @interface dagger.** { *; }
-keep @interface androidx.room.** { *; }

# Logging
-keep class com.hazrat.islam24.core.data.entity.** { *; }
-keep class com.hazrat.islam24.core.domain.model.** { *; }
