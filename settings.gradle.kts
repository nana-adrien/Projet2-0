rootProject.name = "Projet2-0"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":shared")
include(":shared-contracts")
include(":androidApp")

include(
    ":server:common",
    ":server:user",
    ":server:settings",
    ":server:notifications",
    ":server:clubs",
    ":server:deuxzero",
    ":server:calendrier",
    ":server:app",
)

include(
    ":core:domain",
    ":core:data",
    ":core:presentation",
    ":core:design_system",
    ":core:config",
)

include(
    ":feature:auth:domain",
    ":feature:auth:data",
    ":feature:auth:presentation",
    ":feature:auth:config",

    ":feature:profile:domain",
    ":feature:profile:data",
    ":feature:profile:presentation",
    ":feature:profile:config",

    ":feature:notifications:domain",
    ":feature:notifications:data",
    ":feature:notifications:presentation",
    ":feature:notifications:config",

    ":feature:settings:domain",
    ":feature:settings:data",
    ":feature:settings:presentation",
    ":feature:settings:config",

    ":feature:dashboard:presentation",
    ":feature:dashboard:config",

    ":feature:clubs:domain",
    ":feature:clubs:data",
    ":feature:clubs:presentation",
    ":feature:clubs:config",

    ":feature:deuxzero:domain",
    ":feature:deuxzero:data",
    ":feature:deuxzero:presentation",
    ":feature:deuxzero:config",
)