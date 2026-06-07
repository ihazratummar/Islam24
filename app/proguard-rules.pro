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

# Prevent R8 from removing internal location classes
-keep class com.google.android.gms.internal.location.** { *; }
-keep class com.google.android.gms.location.** { *; }
-dontwarn com.google.android.gms.internal.location.**
-dontwarn com.google.android.gms.location.**


# Google Maps SDK
-keep class com.google.android.gms.maps.** { *; }
-keep class com.google.maps.android.** { *; }

# Keep dynamically referenced classes for reflection
-keepnames class com.google.android.gms.** { *; }
-keepclassmembers class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
-dontwarn com.google.android.gms.**

# Parcelable Support
-keep class * implements android.os.Parcelable { *; }
-keepclassmembers class * {
    public static final android.os.Parcelable$Creator *;
}

# SafeParcelable annotations for Play Services
-keep class com.google.android.gms.common.internal.safeparcel.SafeParcelable { *; }
-keep class * extends com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable { *; }
-keep class * implements com.google.android.gms.common.internal.safeparcel.SafeParcelable { *; }
-keepclassmembers class * {
    @com.google.android.gms.common.internal.safeparcel.SafeParcelable$Field <fields>;
}
-keepclassmembers class * {
    @com.google.android.gms.common.internal.safeparcel.SafeParcelable$Constructor <init>(...);
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


# Room
-keep class androidx.room.** { *; }
-keep class androidx.sqlite.db.** { *; }
-keep class androidx.arch.core.executor.** { *; }
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.room.**
-dontwarn androidx.sqlite.db.**
-dontwarn androidx.arch.core.executor.**
-dontwarn androidx.lifecycle.**
-keep class androidx.core.** { *; }

# Keep classes accessed via reflection
-keep class * { @androidx.room.Entity *; }
-keep class * { @androidx.room.Dao *; }


# Keep annotation classes
-keep @interface javax.inject.** { *; }
-keep @interface dagger.** { *; }
-keep @interface androidx.room.** { *; }

# Logging
# Coil
-keep class coil.** { *; }
-keep class coil.RealImageLoader { *; }
-dontwarn coil.gif.GifDecoder
-dontwarn com.airbnb.lottie.**

# OneSignal
-keep class com.onesignal.** { *; }

# Lottie
-keep class com.airbnb.lottie.** { *; }


# Keep all data classes
-keep class * extends kotlin.coroutines.jvm.internal.SuspendLambda
-keep class * implements kotlinx.coroutines.flow.FlowCollector


# Keep Hilt view models
-keep class * extends androidx.lifecycle.ViewModel
-keep class com.hazrat.islam24.core.data.entity.** { *; }
-keep class com.hazrat.islam24.core.domain.model.** { *; }
-keep class com.hazrat.islam24.auth.model.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Firebase Firestore
-keep class com.google.firebase.firestore.** { *; }
-dontwarn com.google.firebase.firestore.**

# Firebase Auth
-keep class com.google.firebase.auth.** { *; }
-dontwarn com.google.firebase.auth.**

# Play Services Auth (Fix R8 Invalid Stack Map Table warnings)
-keep class com.google.android.gms.auth.api.identity.** { *; }
-keep class com.google.android.gms.auth.api.signin.internal.** { *; }
-dontwarn com.google.android.gms.auth.api.identity.**
-dontwarn com.google.android.gms.auth.api.signin.internal.**

# Firebase Storage
-keep class com.google.firebase.storage.** { *; }
-dontwarn com.google.firebase.storage.**

# Kotlin Serialization
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# Ensure that metadata related to Kotlin reflection is kept
-keepclassmembers class kotlin.Metadata { *; }


-keep class com.github.msarhan.ummalqura.calendar.** { *; }
-keep class com.github.eltohamy.materialhijricalendarview.** { *; }
-keep class com.github.kizitonwose.calendar.** { *; }

-keep class com.hazrat.ui.theme.Dimens.**{ *; }
-keep class com.hazrat.ui.theme.ThemeKt.**{ *; }
-keep class com.hazrat.ui.theme.TypeKt.**{ *; }
