plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            api(project(":feature:deuxzero:domain"))
            api(project(":feature:calendrier:domain"))
            api(project(":feature:matchendirect:domain"))
        }
    }
}
