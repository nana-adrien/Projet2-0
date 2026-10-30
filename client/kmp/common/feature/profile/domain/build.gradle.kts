plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:core:domain"))
        }
    }
}
