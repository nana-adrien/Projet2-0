plugins {
    alias(libs.plugins.conventionCmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:core:domain"))
        }
    }
}
