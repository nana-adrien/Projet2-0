plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":client:kmp:common:core:domain"))
            api(project(":client:kmp:common:core:data"))
            implementation(project(":client:kmp:common:feature:matchendirect:domain"))
            implementation(project(":client:kmp:common:feature:calendrier:data"))
            implementation(projects.clientServeurContrat)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.websockets)
        }
    }
}
