plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:clubs:domain"))
            api(project(":feature:clubs:data"))
            api(project(":feature:clubs:presentation"))
        }
    }
}
