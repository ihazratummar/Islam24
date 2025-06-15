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

# Keep annotation classes
-keep @interface javax.inject.** { *; }
-keep @interface dagger.** { *; }
-keep @interface androidx.room.** { *; }

# Dagger Hilt
-keep class dagger.hilt.** { *; }
-keep interface dagger.hilt.** { *; }

# Keep Hilt generated components
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }

# Keep annotated Hilt modules and components
-keep @dagger.Module class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }

# Prevent R8 from removing Hilt-injected classes
-keep class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <fields>;
}


# Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.moshi.** { *; }
-keep class com.google.gson.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn com.squareup.moshi.**
-dontwarn com.google.gson.**

# Retrofit interfaces
-keep interface * {
    @retrofit2.http.* <methods>;
}

# GSON
-keep class com.google.gson.** { *; }
-keep class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepattributes *Annotation*

# Keep model classes (optional but helpful)
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Jetpack DataStore (safe default)
-dontwarn androidx.datastore.**
-keep class androidx.datastore.** { *; }

# Preference KTX
-dontwarn androidx.preference.**
-keep class androidx.preference.** { *; }