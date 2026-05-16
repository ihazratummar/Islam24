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

# Keep everything in Umm al-Qura calendar library
-keep class com.github.msarhan.ummalqura.calendar.** { *; }

# Keep everything in Hijri calendar (adjust if using another package)
-keep class com.github.eltohamy.materialhijricalendarview.** { *; }

# If using kiziton/hijricalendar-view
-keep class com.github.kizitonwose.calendar.** { *; }

# Keep Kotlin Metadata (used for reflection and Compose)
-keep class kotlin.Metadata { *; }

# Preserve Compose functionality
#-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-dontwarn androidx.compose.**


# Keep Kotlin metadata for Compose
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

# Prevent Compose class removal
-keep class *Kt {
    *;
}

# Optional: keep all annotations (useful for Room, Hilt, etc.)
-keepattributes *Annotation*

# Dagger Hilt
-keep class dagger.hilt.** { *; }
-keep interface dagger.hilt.** { *; }
