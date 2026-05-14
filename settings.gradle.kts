pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Islam-24"
include(":app")

include(":core:ui")
include(":core:utils")
include(":core:sensor")
include(":core:remote")
include(":core:location")
include(":core:database")
include(":core:datastore")
include(":core:downloader")
include(":core:permission")
include(":core:notification")

include(":domain:model")
include(":domain:usecase")
include(":domain:repository")

include(":feature:zakat")
include(":feature:common")
include(":feature:calendar")

include(":feature:auth:ui")
include(":feature:auth:data")
include(":feature:auth:domain")

include(":feature:qibla:ui")
include(":feature:qibla:data")

include(":feature:allahNames:ui")
include(":feature:allahNames:data")
include(":feature:allahNames:domain")

include(":feature:athkar:ui")
include(":feature:athkar:data")
include(":feature:athkar:domain")

include(":feature:alQuran:ui")
include(":feature:alQuran:data")
include(":feature:alQuran:domain")

include(":feature:prayertime:ui")
include(":feature:prayertime:data")

include(":feature:home:ui")
