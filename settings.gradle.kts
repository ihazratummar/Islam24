pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
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
include(":feature:calendar")
include(":core:ui")
include(":feature:common")
include(":core:di")
include(":core:utils")
include(":core:datastore")
include(":domain:model")
include(":core:database")
include(":feature:zakat")
