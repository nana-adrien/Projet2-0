plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:core:domain"))
            api(project(":client:kmp:common:feature:calendrier:domain"))
            api(project(":client:kmp:common:feature:deuxzero:domain"))
            api(project(":client:kmp:common:feature:matchendirect:domain"))
        }
    }
}
