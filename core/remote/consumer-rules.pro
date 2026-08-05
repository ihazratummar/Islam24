# Ktor HTTP Client Rules
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
-dontwarn java.lang.management.**
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn org.slf4j.**
-dontwarn io.netty.**

# Remote DTOs (Kotlinx Serialization)
-keep class com.hazrat.remote.dto.** { *; }
-keepclassmembers class com.hazrat.remote.dto.** { *; }
