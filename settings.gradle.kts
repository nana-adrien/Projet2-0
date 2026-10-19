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
    ":server:user:infra",
    ":server:user:service",
    ":server:user:api",
    ":server:user:config",
    ":server:settings:infra",
    ":server:settings:service",
    ":server:settings:api",
    ":server:settings:config",
    ":server:notifications:infra",
    ":server:notifications:service",
    ":server:notifications:api",
    ":server:notifications:config",
    ":server:clubs:infra",
    ":server:clubs:service",
    ":server:clubs:api",
    ":server:clubs:config",
    ":server:deuxzero:infra",
    ":server:deuxzero:service",
    ":server:deuxzero:api",
    ":server:deuxzero:config",
    ":server:calendrier:infra",
    ":server:calendrier:service",
    ":server:calendrier:api",
    ":server:calendrier:websocket",
    ":server:calendrier:config",
    ":server:statistiques:infra",
    ":server:statistiques:service",
    ":server:statistiques:api",
    ":server:statistiques:config",
    ":server:amical:service",
    ":server:amical:api",
    ":server:amical:config",
    ":server:social:infra",
    ":server:social:service",
    ":server:social:api",
    ":server:social:config",
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

    ":feature:calendrier:domain",
    ":feature:calendrier:data",
    ":feature:calendrier:presentation",
    ":feature:calendrier:config",

    ":feature:matchendirect:domain",
    ":feature:matchendirect:data",
    ":feature:matchendirect:presentation",
    ":feature:matchendirect:config",

    ":feature:validation:domain",
    ":feature:validation:data",
    ":feature:validation:presentation",
    ":feature:validation:config",

    ":feature:statistiques:domain",
    ":feature:statistiques:data",
    ":feature:statistiques:presentation",
    ":feature:statistiques:config",

    ":feature:amical:domain",
    ":feature:amical:data",
    ":feature:amical:presentation",
    ":feature:amical:config",

    ":feature:social:domain",
    ":feature:social:data",
    ":feature:social:presentation",
    ":feature:social:config",
)