plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            api(project(":feature:clubs:domain"))
        }
    }
}
