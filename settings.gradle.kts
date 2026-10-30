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

include(":client:kmp:common:shared")
include(":shared-contracts")
include(":client:kmp:mobile:androidApp")

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
    ":client:kmp:common:core:domain",
    ":client:kmp:common:core:data",
    ":client:kmp:common:core:presentation",
    ":client:kmp:common:core:design_system",
    ":client:kmp:common:core:config",
)

include(
    ":client:kmp:common:feature:auth:domain",
    ":client:kmp:common:feature:auth:data",
    ":client:kmp:common:feature:auth:presentation",
    ":client:kmp:common:feature:auth:config",

    ":client:kmp:common:feature:profile:domain",
    ":client:kmp:common:feature:profile:data",
    ":client:kmp:common:feature:profile:presentation",
    ":client:kmp:common:feature:profile:config",

    ":client:kmp:common:feature:notifications:domain",
    ":client:kmp:common:feature:notifications:data",
    ":client:kmp:common:feature:notifications:presentation",
    ":client:kmp:common:feature:notifications:config",

    ":client:kmp:common:feature:settings:domain",
    ":client:kmp:common:feature:settings:data",
    ":client:kmp:common:feature:settings:presentation",
    ":client:kmp:common:feature:settings:config",

    ":client:kmp:common:feature:dashboard:presentation",
    ":client:kmp:common:feature:dashboard:config",

    ":client:kmp:common:feature:clubs:domain",
    ":client:kmp:common:feature:clubs:data",
    ":client:kmp:common:feature:clubs:presentation",
    ":client:kmp:common:feature:clubs:config",

    ":client:kmp:common:feature:deuxzero:domain",
    ":client:kmp:common:feature:deuxzero:data",
    ":client:kmp:common:feature:deuxzero:presentation",
    ":client:kmp:common:feature:deuxzero:config",

    ":client:kmp:common:feature:calendrier:domain",
    ":client:kmp:common:feature:calendrier:data",
    ":client:kmp:common:feature:calendrier:presentation",
    ":client:kmp:common:feature:calendrier:config",

    ":client:kmp:common:feature:matchendirect:domain",
    ":client:kmp:common:feature:matchendirect:data",
    ":client:kmp:common:feature:matchendirect:presentation",
    ":client:kmp:common:feature:matchendirect:config",

    ":client:kmp:common:feature:validation:domain",
    ":client:kmp:common:feature:validation:data",
    ":client:kmp:common:feature:validation:presentation",
    ":client:kmp:common:feature:validation:config",

    ":client:kmp:common:feature:statistiques:domain",
    ":client:kmp:common:feature:statistiques:data",
    ":client:kmp:common:feature:statistiques:presentation",
    ":client:kmp:common:feature:statistiques:config",

    ":client:kmp:common:feature:amical:domain",
    ":client:kmp:common:feature:amical:data",
    ":client:kmp:common:feature:amical:presentation",
    ":client:kmp:common:feature:amical:config",

    ":client:kmp:common:feature:social:domain",
    ":client:kmp:common:feature:social:data",
    ":client:kmp:common:feature:social:presentation",
    ":client:kmp:common:feature:social:config",
)
